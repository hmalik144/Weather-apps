package com.appttude.h_mal.atlas_weather.data

import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.getSymbol
import java.io.IOException

class WeatherSource(
    val repository: Repository,
    private val locationProvider: LocationProvider
) {

    @Throws(IOException::class)
    suspend fun getWeather(
        latLon: Pair<Double, Double>,
        locationName: String? = null,
        locationType: LocationType = LocationType.Town
    ): FullWeather {
        val location = locationName ?: CURRENT_LOCATION

        // Has the search been conducted in the last 5 minutes
        return if (repository.isSearchValid(location)) {
            fetchWeather(latLon, location, locationType)
        } else {
            val weather = repository.getSingleWeather(location)
            repository.saveCurrentWeatherToRoom(weather)
            weather.weather
        }
    }

    @Throws(IOException::class)
    suspend fun forceFetchWeather(
        latLon: Pair<Double, Double>,
        locationType: LocationType = LocationType.Town
    ): FullWeather {
        // get data from database
        val weatherEntity = repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION)
        // check unit type - if same do nothing
        val units = repository.getUnitType().getSymbol()
        if (weatherEntity.weather.temperatureUnit == units) return weatherEntity.weather
        // load data for forced
        return fetchWeather(
            Pair(latLon.first, latLon.second),
            CURRENT_LOCATION, locationType
        )
    }

    private suspend fun fetchWeather(
        latLon: Pair<Double, Double>,
        locationName: String,
        locationType: LocationType = LocationType.Town
    ): FullWeather {
        // Get weather from api
        val weather = repository
            .getWeatherFromApi(latLon.first.toString(), latLon.second.toString())
        val lat = weather.latitude ?: latLon.first
        val long = weather.longitude ?: latLon.second
        val currentLocation =
            locationProvider.getLocationNameFromLatLong(lat, long, locationType)
        val unit = repository.getUnitType().getSymbol()
        val fullWeather = weather.mapData().apply {
            temperatureUnit = unit
            locationString = currentLocation
        }
        val entityItem = EntityItem(locationName, fullWeather)
        // Save data if not null
        repository.saveLastSavedAt(locationName)
        repository.saveCurrentWeatherToRoom(entityItem)

        return fullWeather
    }
}