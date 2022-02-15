package com.appttude.h_mal.atlas_weather.helper

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetCellData
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetWeatherCollection
import com.appttude.h_mal.atlas_weather.utils.toSmallDayName
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ServicesHelper(
        private val repository: Repository,
        private val settingsRepository: SettingsRepository,
        private val locationProvider: LocationProvider
) {

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    suspend fun fetchData(): Boolean {
        if (!repository.isSearchValid(CURRENT_LOCATION)) return false

        return try {
            // Get location
            val latLong = locationProvider.getCurrentLatLong()
            // Get weather from api
            val weather = repository
                    .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())
            val currentLocation = locationProvider.getLocationNameFromLatLong(weather.lat, weather.lon)
            val fullWeather = FullWeather(weather).apply {
                temperatureUnit = "°C"
                locationString = currentLocation
            }
            val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)
            // Save data if not null
            repository.saveLastSavedAt(CURRENT_LOCATION)
            repository.saveCurrentWeatherToRoom(entityItem)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getWidgetWeather(): WidgetData? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)
            val epoc = System.currentTimeMillis()
            
            result.weather.let {
                val bitmap = it.current?.icon
                val location = locationProvider.getLocationNameFromLatLong(it.lat, it.lon)
                val temp = it.current?.temp?.toInt().toString()

                WidgetData(location, bitmap, temp, epoc)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWidgetInnerWeather(): List<InnerWidgetData>? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)
            val list = mutableListOf<InnerWidgetData>()

            result.weather.daily?.drop(1)?.dropLast(2)?.forEach { dailyWeather ->
                val day = dailyWeather.dt?.toSmallDayName()
                val bitmap = withContext(Dispatchers.Main) {
                    getBitmapFromUrl(dailyWeather.icon)
                }
                val temp = dailyWeather.max?.toInt().toString()

                val item = InnerWidgetData(day, bitmap, temp)
                list.add(item)
            }
            list.toList()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWidgetWeatherCollection(): WidgetWeatherCollection? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)

            val widgetData = result.weather.let {
                val bitmap = it.current?.icon
                val location = locationProvider.getLocationNameFromLatLong(it.lat, it.lon)
                val temp = it.current?.temp?.toInt().toString()
                val epoc = System.currentTimeMillis()

                WidgetData(location, bitmap, temp, epoc)
            }

            val list = mutableListOf<InnerWidgetCellData>()

            result.weather.daily?.drop(1)?.dropLast(2)?.forEach { dailyWeather ->
                val day = dailyWeather.dt?.toSmallDayName()
                val icon = dailyWeather.icon
                val temp = dailyWeather.max?.toInt().toString()

                val item = InnerWidgetCellData(day, icon, temp)
                list.add(item)
            }
            list.toList()

            WidgetWeatherCollection(widgetData, list)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getBitmapFromUrl(imageAddress: String?): Bitmap? {
        return suspendCoroutine { cont ->
            Picasso.get().load(imageAddress).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    cont.resume(bitmap)
                }

                override fun onBitmapFailed(e: Exception?, d: Drawable?) {
                    cont.resume(null)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
    }

    fun getWidgetBackground(): Int {
        return if (settingsRepository.isBlackBackground())
            Color.BLACK
        else
            Color.TRANSPARENT
    }
}