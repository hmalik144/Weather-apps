package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.data.network.response.weather.Days
import com.appttude.h_mal.atlas_weather.model.IconMapper
import com.appttude.h_mal.atlas_weather.utils.generateIconUrlString


data class DailyWeather(
    val dt: Int?,
    val sunrise: Int?,
    val sunset: Int?,
    val min: Double? = null,
    val max: Double? = null,
    val average: Double? = null,
    var feelsLike: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val dewPoint: Double?,
    val windSpeed: Double?,
    val windDeg: Int?,
    val icon: String? = null,
    val description: String? = null,
    val main: String? = null,
    val id: Int? = null,
    val clouds: Int?,
    val pop: Double?,
    val uvi: Double?,
    val rain: Double?
) {

    constructor(days: Days) : this(
        days.datetimeEpoch,
        days.sunriseEpoch,
        days.sunsetEpoch,
        days.tempmin,
        days.tempmax,
        days.temp,
        days.feelslike,
        days.pressure?.toInt(),
        days.humidity?.toInt(),
        days.dew,
        days.windspeed,
        days.winddir?.toInt(),
        generateIconUrlString(
            IconMapper.findIconCode(days.icon)
        ),
        days.description,
        days.conditions,
        days.datetimeEpoch,
        days.cloudcover?.toInt(),
        days.precipprob,
        days.uvindex,
        days.precipprob
    )

}