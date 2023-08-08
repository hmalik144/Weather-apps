package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse

data class FullWeather(
    val current: Current? = null,
    val timezone: String? = null,
    val timezoneOffset: Int? = null,
    val hourly: List<Hour>? = null,
    val daily: List<DailyWeather>? = null,
    val lon: Double = 0.00,
    val lat: Double = 0.00,
    var locationString: String? = null,
    var temperatureUnit: String? = null
) {

    constructor(weatherResponse: WeatherResponse) : this(
        weatherResponse.current?.let { Current(it) },
        weatherResponse.timezone,
        weatherResponse.timezoneOffset,
        weatherResponse.hourly?.subList(0, 23)?.map { Hour(it) },
        weatherResponse.daily?.map { DailyWeather(it) },
        weatherResponse.lon,
        weatherResponse.lat
    )

}


