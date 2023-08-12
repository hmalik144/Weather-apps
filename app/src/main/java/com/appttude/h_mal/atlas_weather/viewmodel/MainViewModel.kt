package com.appttude.h_mal.atlas_weather.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.viewmodel.baseViewModels.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val locationProvider: LocationProvider,
    private val repository: Repository
) : WeatherViewModel() {

    init {
        repository.loadCurrentWeatherFromRoom(CURRENT_LOCATION).observeForever {
            it?.let {
                val weather = WeatherDisplay(it)
                onSuccess(weather)
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    fun fetchData() {
        onStart()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Has the search been conducted in the last 5 minutes
                val entityItem = if (repository.isSearchValid(CURRENT_LOCATION)) {
                    // Get location
                    val latLong = locationProvider.getCurrentLatLong()
                    // Get weather from api
                    val weather = repository
                        .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())
                    val currentLocation =
                        locationProvider.getLocationNameFromLatLong(weather.lat, weather.lon)
                    val fullWeather = createFullWeather(weather, currentLocation)
                    EntityItem(CURRENT_LOCATION, fullWeather)
                } else {
                    repository.getSingleWeather(CURRENT_LOCATION)
                }
                // Save data if not null
                repository.saveLastSavedAt(CURRENT_LOCATION)
                repository.saveCurrentWeatherToRoom(entityItem)
            } catch (e: Exception) {
                onError(e.message!!)
            }
        }
    }
}