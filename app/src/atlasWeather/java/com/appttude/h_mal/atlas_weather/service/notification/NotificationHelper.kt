package com.appttude.h_mal.atlas_weather.service.notification

import android.Manifest
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather

class NotificationHelper(
    private val weatherSource: WeatherSource,
    private val locationProvider: LocationProvider
) {

    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    suspend fun fetchData(): FullWeather? {
        return try {
            // Get location
            val latLong = locationProvider.getCurrentLatLong()
            weatherSource.getWeather(latLon = latLong)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}