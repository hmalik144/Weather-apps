package com.appttude.h_mal.atlas_weather.helper

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetCellData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetError
import com.appttude.h_mal.atlas_weather.model.widget.WidgetState
import com.appttude.h_mal.atlas_weather.model.widget.WidgetWeatherCollection
import com.appttude.h_mal.atlas_weather.utils.getSymbol
import com.appttude.h_mal.atlas_weather.utils.toSmallDayName
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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
            val lat = weather.latitude ?: latLong.first
            val long = weather.longitude ?: latLong.second
            val currentLocation =
                locationProvider.getLocationNameFromLatLong(lat, long)
            val fullWeather = weather.mapData().apply {
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

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    suspend fun fetchWidgetData(): WidgetState {
        // Data was loaded within last 5 minutes - no need to retrieve again
        if (!repository.isSearchValid(CURRENT_LOCATION)) {
            val data = getWidgetWeatherCollection()
            data?.let {
                return WidgetState.HasData(it)
            }
        }

        // Try and retrieve location
        val latLong = try {
            locationProvider.getCurrentLatLong()
        } catch (e: IOException) {
            val data = getWidgetWeatherCollection()
            data?.let {
                return WidgetState.HasData(it)
            }

            val error = WidgetError(
                icon = R.drawable.ic_baseline_cloud_off_24,
                errorMessage = "Failed to retrieve location, check location"
            )
            return WidgetState.HasError(error)
        }

        // Get weather from api
        val weather = try {
            repository
                .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())
        } catch (e: IOException) {
            val data = getWidgetWeatherCollection()
            data?.let {
                return WidgetState.HasData(it)
            }

            val error = WidgetError(
                icon = R.drawable.ic_baseline_cloud_off_24,
                errorMessage = "Failed to retrieve weather data, check connection"
            )
            return WidgetState.HasError(error)
        }

        val lat = weather.latitude ?: latLong.first
        val long = weather.longitude ?: latLong.second

        val currentLocation = try {
            locationProvider.getLocationNameFromLatLong(lat, long)
        } catch (e: IOException) {
            val data = getWidgetWeatherCollection()
            data?.let {
                return WidgetState.HasData(it)
            }

            val error = WidgetError(
                icon = R.drawable.ic_baseline_cloud_off_24,
                errorMessage = "Failed to retrieve location name"
            )
            return WidgetState.HasError(error)
        }

        val fullWeather = weather.mapData().apply {
            temperatureUnit = repository.getUnitType().getSymbol()
            locationString = currentLocation
        }
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)
        // Save data to database
        repository.saveLastSavedAt(CURRENT_LOCATION)
        repository.saveCurrentWeatherToRoom(entityItem)

        val data = createWidgetWeatherCollection(entityItem, currentLocation)
        return WidgetState.HasData(data)
    }

    suspend fun getWidgetWeatherCollection(): WidgetWeatherCollection? {
        return try {
            val result = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)

            val widgetData = result.weather.let {
                val bitmap = it.current?.icon
                val location = locationProvider.getLocationNameFromLatLong(it.lat!!, it.lon!!)
                val temp = it.current?.temp?.toInt().toString()
                val epoc = System.currentTimeMillis()

                WidgetData(location, bitmap, temp, epoc)
            }

            val list = mutableListOf<InnerWidgetCellData>()

            result.weather.daily?.drop(1)?.dropLast(1)?.forEach { dailyWeather ->
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

    private fun createWidgetWeatherCollection(
        result: EntityItem,
        locationName: String
    ): WidgetWeatherCollection {
        val widgetData = result.weather.let {
            val bitmap = it.current?.icon
            val temp = it.current?.temp?.toInt().toString()
            val epoc = System.currentTimeMillis()

            WidgetData(locationName, bitmap, temp, epoc)
        }

        val list = mutableListOf<InnerWidgetCellData>()

        result.weather.daily?.drop(1)?.dropLast(1)?.forEach { dailyWeather ->
            val day = dailyWeather.dt?.toSmallDayName()
            val icon = dailyWeather.icon
            val temp = dailyWeather.max?.toInt().toString()

            val item = InnerWidgetCellData(day, icon, temp)
            list.add(item)
        }
        list.toList()

        return WidgetWeatherCollection(widgetData, list)
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