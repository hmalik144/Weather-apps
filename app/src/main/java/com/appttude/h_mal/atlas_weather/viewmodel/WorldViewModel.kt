package com.appttude.h_mal.atlas_weather.viewmodel

import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.model.types.LocationType
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
    private var currentLocation: String? = null

    private val weatherListLiveData = repository.loadRoomWeatherLiveData()

    init {
        weatherListLiveData.observeForever {
            val list = it.map { data ->
                WeatherDisplay(data)
            }
            onSuccess(list)
            currentLocation?.let { i -> list.first { j -> j.location == i } }
                ?.let { k -> onSuccess(k) }
        }
    }

    fun setLocation(location: String) = run { currentLocation = location }

    fun getSingleLocation(locationName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = repository.getSingleWeather(locationName)
            val item = WeatherDisplay(entity)
            onSuccess(item)
        }
    }

    fun fetchDataForSingleLocation(locationName: String) {
        onStart()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherEntity = if (repository.isSearchValid(locationName)) {
                    createWeatherEntity(locationName)
                } else {
                    repository.getSingleWeather(locationName)
                }
                onSuccess(Unit)
                repository.saveCurrentWeatherToRoom(weatherEntity)
                repository.saveLastSavedAt(weatherEntity.id)
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    fun fetchDataForSingleLocationSearch(locationName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onStart()
            // Check if location exists
            if (repository.getSavedLocations().contains(locationName)) {
                onError("$locationName already exists")
                return@launch
            }

            try {
                // Get weather from api
                val entityItem = createWeatherEntity(locationName)

                // retrieved location name
                val retrievedLocation = locationProvider.getLocationNameFromLatLong(
                    entityItem.weather.lat,
                    entityItem.weather.lon,
                    LocationType.City
                )
                if (repository.getSavedLocations().contains(retrievedLocation)) {
                    onError("$retrievedLocation already exists")
                    return@launch
                }
                // Save data if not null
                repository.saveCurrentWeatherToRoom(entityItem)
                repository.saveLastSavedAt(retrievedLocation)
                onSuccess("$retrievedLocation saved")
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    fun fetchAllLocations() {
        onStart()
        if (!repository.isSearchValid(ALL_LOADED)) {
            onSuccess(Unit)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = mutableListOf<EntityItem>()
                repository.loadWeatherList().forEach { locationName ->
                    // If search not valid move onto next in loop
                    if (!repository.isSearchValid(locationName)) return@forEach

                    try {
                        val entity = createWeatherEntity(locationName)
                        list.add(entity)
                        repository.saveLastSavedAt(locationName)
                    } catch (e: IOException) {
                    }
                }
                repository.saveWeatherListToRoom(list)
                repository.saveLastSavedAt(ALL_LOADED)
            } catch (e: IOException) {
                onError(e.message!!)
            } finally {
                onSuccess(Unit)
            }
        }
    }

    fun deleteLocation(locationName: String) {
        onStart()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repository.deleteSavedWeatherEntry(locationName)
                if (!success) {
                    onError("Failed to delete")
                }
            } catch (e: IOException) {
                onError(e.message!!)
            } finally {
                onSuccess(Unit)
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
        val location =
            locationProvider.getLocationNameFromLatLong(weather.lat, weather.lon, LocationType.City)
        val fullWeather = createFullWeather(weather, location)
        return createWeatherEntity(location, fullWeather)
    }
}