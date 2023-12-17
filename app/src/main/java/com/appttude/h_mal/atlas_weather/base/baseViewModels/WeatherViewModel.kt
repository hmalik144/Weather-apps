package com.appttude.h_mal.atlas_weather.base.baseViewModels

import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.room.entity.WeatherEntity
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather

abstract class WeatherViewModel : BaseViewModel() {

    fun createFullWeather(
        weather: WeatherResponse,
        locationName: String
    ): FullWeather {
        return FullWeather(weather).apply {
            temperatureUnit = "°C"
            locationString = locationName
        }
    }

    fun createWeatherEntity(
        locationName: String,
        weather: FullWeather
    ): WeatherEntity {
        weather.apply {
            locationString = locationName
        }

        return WeatherEntity(locationName, weather)
    }
}