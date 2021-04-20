package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.lifecycle.MutableLiveData
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.utils.Event
import com.appttude.h_mal.atlas_weather.viewmodel.baseViewModels.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

const val ALL_LOADED = "all_loaded"
class WorldViewModel(
        private val locationProvider: LocationProvider,
        private val repository: Repository
) : WeatherViewModel() {

    val weatherLiveData = MutableLiveData<List<WeatherDisplay>>()
    val singleWeatherLiveData = MutableLiveData<WeatherDisplay>()

    val operationState = MutableLiveData<Event<Boolean>>()
    val operationError = MutableLiveData<Event<String>>()
    val operationRefresh = MutableLiveData<Event<Boolean>>()

    val operationComplete = MutableLiveData<Event<String>>()

    private val weatherListLiveData = repository.loadRoomWeatherLiveData()

    init {
        weatherListLiveData.observeForever {
            val list = it.map { data ->
                WeatherDisplay(data)
            }
            weatherLiveData.postValue(list)
        }
    }

    fun getSingleLocation(locationName: String){
        CoroutineScope(Dispatchers.IO).launch {
            val entity = repository.getSingleWeather(locationName)
            val item = WeatherDisplay(entity)
            singleWeatherLiveData.postValue(item)
        }
    }

    fun fetchDataForSingleLocation(locationName: String) {
        if (!repository.isSearchValid(locationName)){
            operationRefresh.postValue(Event(false))
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                val weatherEntity = createWeatherEntity(locationName)
                repository.saveCurrentWeatherToRoom(weatherEntity)
                repository.saveLastSavedAt(locationName)
            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
                operationRefresh.postValue(Event(false))
            }
        }
    }

    fun fetchDataForSingleLocationSearch(locationName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            // Check if location exists
            if (repository.getSavedLocations().contains(locationName)){
                operationError.postValue(Event("$locationName already exists"))
                return@launch
            }

            try {
                // Get weather from api
                val entityItem = createWeatherEntity(locationName)

                // retrieved location name
                val retrievedLocation = locationProvider.getLocationNameFromLatLong(entityItem.weather.lat, entityItem.weather.lon, LocationType.City)
                if (repository.getSavedLocations().contains(retrievedLocation)){
                    operationError.postValue(Event("$retrievedLocation already exists"))
                    return@launch
                }
                // Save data if not null
                repository.saveCurrentWeatherToRoom(entityItem)
                repository.saveLastSavedAt(retrievedLocation)
                operationComplete.postValue(Event("$retrievedLocation saved"))

            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
            }
        }
    }

    fun fetchAllLocations() {
        if (!repository.isSearchValid(ALL_LOADED)){
            operationRefresh.postValue(Event(false))
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                val list = mutableListOf<EntityItem>()
                repository.loadWeatherList().forEach { locationName ->
                    // If search not valid move onto next in loop
                    if (!repository.isSearchValid(locationName)) return@forEach

                    try {
                        val entity = createWeatherEntity(locationName)
                        list.add(entity)
                        repository.saveLastSavedAt(locationName)
                    } catch (e: IOException) { }
                }
                repository.saveWeatherListToRoom(list)
                repository.saveLastSavedAt(ALL_LOADED)
            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
            }
        }
    }

    fun deleteLocation(locationName: String){
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                val success = repository.deleteSavedWeatherEntry(locationName)
                if (!success){
                    operationError.postValue(Event("Failed to delete"))
                }
            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
            }
        }
    }

    private suspend fun getWeather(locationName: String): WeatherResponse {
        // Get location
        val latLong =
                locationProvider.getLatLongFromLocationName(locationName)
        val lat = latLong.first
        val lon = latLong.second

        // Get weather from api
        return repository.getWeatherFromApi(lat.toString(), lon.toString())
    }

    private suspend fun createWeatherEntity(locationName: String): EntityItem {
        val weather = getWeather(locationName)
        val location = locationProvider.getLocationNameFromLatLong(weather.lat, weather.lon, LocationType.City)
        val fullWeather = createFullWeather(weather, location)
        return createWeatherEntity(location,fullWeather)
    }
}