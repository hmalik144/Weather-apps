package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.data.network.response.weather.CurrentConditions
import com.appttude.h_mal.atlas_weather.model.IconMapper
import com.appttude.h_mal.atlas_weather.utils.generateIconUrlString

data class Current(
    val dt: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val temp: Double? = null,
    val visibility: Int? = null,
    val uvi: Double? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Double? = null,
    val windDeg: Int? = null,
    val dewPoint: Double? = null,
    val icon: String? = null,
    val description: String? = null,
    val main: String? = null,
    val id: Int? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null
) {

    constructor(currentConditions: CurrentConditions?) : this(
        dt = currentConditions?.datetimeEpoch,
        sunrise = currentConditions?.sunriseEpoch,
        sunset = currentConditions?.sunsetEpoch,
        temp = currentConditions?.temp,
        visibility = currentConditions?.visibility?.toInt(),
        uvi = currentConditions?.uvindex?.toDouble(),
        pressure = currentConditions?.pressure?.toInt(),
        clouds = currentConditions?.cloudcover?.toInt(),
        feelsLike = currentConditions?.feelslike,
        windDeg = currentConditions?.winddir?.toInt(),
        dewPoint = currentConditions?.dew,
        icon = generateIconUrlString(IconMapper.findIconCode(currentConditions?.icon)),
        description = currentConditions?.conditions,
        main = currentConditions?.conditions,
        id = currentConditions?.datetimeEpoch,
        humidity = currentConditions?.humidity?.toInt(),
        windSpeed = currentConditions?.windspeed
    )
}