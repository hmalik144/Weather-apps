package com.appttude.h_mal.atlas_weather.notification

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetData
import com.appttude.h_mal.atlas_weather.utils.toSmallDayName
import java.io.IOException
import java.net.URL


class ServicesHelper(
        private val repository: Repository,
        private val settingsRepository: SettingsRepository,
        private val locationProvider: LocationProvider
) {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getData(): FullWeather? {
        return try {
            val latLon = locationProvider.getLatLong()
            val result =
                    repository.getWeatherFromApi(
                            latLon.first.toString(),
                            latLon.second.toString()
                    )
            FullWeather(result)
        } catch (e: Exception) {

            null
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun fetchData(): Boolean {
        if (!repository.isSearchValid(CURRENT_LOCATION)) return true

        return try {
            // Get location
            val latLong = locationProvider.getLatLong()
            // Get weather from api
            val weather = repository
                    .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())

            // Save data if not null
            weather.let {
                repository.saveLastSavedAt(CURRENT_LOCATION)
                repository.saveCurrentWeatherToRoom(CURRENT_LOCATION, it)
            }
            false
        } catch (e: IOException) {
            false
        }
    }

    suspend fun getWidgetWeather(): WidgetData? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)

            result.weather.let {
                WidgetData(
                        locationProvider.getLocationName(it.lat, it.lon),
                        getBitmapFromUrl(it.daily?.get(0)?.icon),
                        it.current?.temp?.toInt().toString()
                )
            }
        } catch (e: Exception) { null }
    }

    suspend fun getWidgetInnerWeather(): List<InnerWidgetData>? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)

            result.weather.daily?.drop(1)?.dropLast(2)?.map{
                InnerWidgetData(
                        it.dt?.toSmallDayName(),
                        getBitmapFromUrl(it.icon),
                        it.max?.toInt().toString()
                )
            }
        } catch (e: Exception) { null }
    }


    fun getBitmapFromUrl(imageAddress: String?): Bitmap? {
        return try {
            val url = URL(imageAddress)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun isEnabled() = settingsRepository.isNotificationsEnabled()

    fun getWidgetBackground(): Int {
        return if (settingsRepository.isBlackBackground()){
            Color.BLACK
        }else{
            Color.TRANSPARENT
        }
    }


    fun setFirstTimer() = settingsRepository.setFirstTime()
}