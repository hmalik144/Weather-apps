package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class WorldViewModel(
        private val locationProvider: LocationProvider,
        private val repository: Repository
) : ViewModel() {

    val weatherLiveData = MutableLiveData<List<WeatherDisplay>>()

    val operationState = MutableLiveData<Event<Boolean>>()
    val operationError = MutableLiveData<Event<String>>()

    val operationComplete = MutableLiveData<Event<String>>()

    private val weatherListLiveData = repository.loadAllWeatherExceptCurrentFromRoom()

    init {
        weatherListLiveData.observeForever {
            val list = it.map { data ->
                WeatherDisplay(data.weather).apply {
                    unit = "°C"
                    location = locationProvider.getLocationName(data.weather.lat, data.weather.lon)
                }
            }
            weatherLiveData.postValue(list)
        }
    }

    fun fetchDataForSingleLocation(locationName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))

            if (repository.getSavedLocations().contains(locationName)){
                operationError.postValue(Event("$locationName already exists"))
                return@launch
            }

            try {
                // Get location
                val latLong =
                        locationProvider.getLatLongFromLocationString(locationName)
                // Get weather from api
                val weather = repository
                        .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())

                // Save data if not null
                weather.let {
                    repository.saveCurrentWeatherToRoom(locationName, it)
                    operationComplete.postValue(Event("$locationName saved"))
                    return@launch
                }
            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
            }
        }
    }

    fun fetchAllLocations() {
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                val list = mutableListOf<EntityItem>()
                weatherLiveData.value?.forEach { weatherItem ->
                    // If search not valid move onto next in loop
                    weatherItem.location?.let {
                        if (!repository.isSearchValid(it)) return@forEach
                    }

                    try {
                        val weather = repository
                                .getWeatherFromApi(weatherItem.lat.toString(), weatherItem.lon.toString())

                        weatherItem.location?.let { loc ->
                            repository.saveLastSavedAt(loc)
                            list.add(EntityItem(loc, FullWeather(weather)))
                        }
                    } catch (e: IOException) { }
                }
                repository.saveWeatherListToRoom(list)
            } catch (e: IOException) {
                operationError.postValue(Event(e.message!!))
            } finally {
                operationState.postValue(Event(false))
            }
        }
    }
}