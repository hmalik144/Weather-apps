package com.appttude.h_mal.atlas_weather.viewmodel

import com.appttude.h_mal.atlas_weather.base.baseViewModels.BaseViewModel
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.IOException

const val ALL_LOADED = "all_loaded"

class WorldViewModel(
    private val locationProvider: LocationProvider,
    private val weatherSource: WeatherSource
) : BaseViewModel() {
    private var currentLocation: String? = null

    init {
        weatherSource.repository.loadRoomWeatherLiveData().observeForever {
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
            val entity = weatherSource.repository.getSingleWeather(locationName)
            val item = WeatherDisplay(entity)
            onSuccess(item)
        }
    }

    fun fetchDataForSingleLocation(locationName: String) {
        onStart()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                searchWeatherForLocation(locationName)
                onSuccess(Unit)
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    fun fetchDataForSingleLocationSearch(locationName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onStart()
            // Check if location already exists
            if (weatherSource.repository.getSavedLocations().contains(locationName)) {
                onError("$locationName already exists")
                return@launch
            }

            try {
                val weather = searchWeatherForLocation(locationName)
                val retrievedLocation = weather.locationString
                // Check if location exists in stored
                if (weatherSource.repository.getSavedLocations().contains(retrievedLocation)) {
                    onError("$retrievedLocation already exists")
                    return@launch
                }
                onSuccess("$retrievedLocation saved")
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    fun fetchAllLocations() {
        onStart()
        if (!weatherSource.repository.isSearchValid(ALL_LOADED)) {
            onSuccess(Unit)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = mutableListOf<Deferred<FullWeather>>()
                weatherSource.repository.loadWeatherList().forEach { locationName ->
                    // If search not valid move onto next in loop
                    if (!weatherSource.repository.isSearchValid(locationName)) return@forEach

                    val task = async{ searchWeatherForLocation(locationName) }
                    list.add(task)
                }
                list.awaitAll()
                onSuccess(Unit)
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    fun deleteLocation(locationName: String) {
        onStart()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = weatherSource.repository.deleteSavedWeatherEntry(locationName)
                if (!success) {
                    onError("Failed to delete")
                } else {
                    onSuccess(Unit)
                }
            } catch (e: IOException) {
                onError(e.message!!)
            }
        }
    }

    private suspend fun searchWeatherForLocation(locationName: String): FullWeather {
        // Get location
        val latLong =
            locationProvider.getLatLongFromLocationName(locationName)
        // Search for location from provider (provider location name maybe different from #locationName)
        val location =
            locationProvider.getLocationNameFromLatLong(
                latLong.first,
                latLong.second,
                LocationType.City
            )
        return weatherSource.getWeather(latLong, location, LocationType.City)
    }
}