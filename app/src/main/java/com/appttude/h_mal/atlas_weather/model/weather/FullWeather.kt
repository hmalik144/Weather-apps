package com.appttude.h_mal.atlas_weather.model.weather

data class FullWeather(
    val current: Current? = null,
    val timezone: String? = null,
    val timezoneOffset: Int? = null,
    val hourly: List<Hour>? = null,
    val daily: List<DailyWeather>? = null,
    val lon: Double? = null,
    val lat: Double? = null,
    var locationString: String? = null,
    var temperatureUnit: String? = null
)


