package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.data.network.response.forecast.DailyItem
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
){

    constructor(dailyItem: DailyItem): this(
            dailyItem.dt,
            dailyItem.sunrise,
            dailyItem.sunset,
            dailyItem.temp?.min,
            dailyItem.temp?.max,
            dailyItem.temp?.day,
            dailyItem.feelsLike?.day,
            dailyItem.pressure,
            dailyItem.humidity,
            dailyItem.dewPoint,
            dailyItem.windSpeed,
            dailyItem.windDeg,
            generateIconUrlString(dailyItem.weather?.getOrNull(0)?.icon),
            dailyItem.weather?.get(0)?.description,
            dailyItem.weather?.get(0)?.main,
            dailyItem.weather?.get(0)?.id,
            dailyItem.clouds,
            dailyItem.pop,
            dailyItem.uvi,
            dailyItem.rain
    )


}