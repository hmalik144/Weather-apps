package com.appttude.h_mal.atlas_weather.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.viewModelScope
import com.appttude.h_mal.atlas_weather.base.baseViewModels.BaseViewModel
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val locationProvider: LocationProvider,
    private val weatherSource: WeatherSource
) : BaseViewModel() {

    init {
        weatherSource.repository.loadCurrentWeatherFromRoom(CURRENT_LOCATION).observeForever { w ->
            w?.let {
                val weather = WeatherDisplay(it)
                onSuccess(weather)
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    fun fetchData() {
        onStart()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                // Get location
                val latLong = locationProvider.getCurrentLatLong()
                weatherSource.getWeather(latLon = latLong)
            } catch (e: Exception) {
                onError(e.message ?: "Retrieving weather failed")
            }
        }
    }
}