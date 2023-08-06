package com.appttude.h_mal.model.forecast

import android.os.Parcelable
import com.appttude.h_mal.model.weather.DailyWeather
import com.appttude.h_mal.utils.toDayName
import com.appttude.h_mal.utils.toDayString
import com.appttude.h_mal.utils.toTime
import kotlinx.parcelize.Parcelize


@Parcelize
data class Forecast(
    val date: String?,
    val day: String?,
    val condition: String?,
    val weatherIcon: String?,
    val mainTemp: String?,
    val minorTemp: String?,
    val averageTemp: String?,
    val windText: String?,
    val precipitation: String?,
    val humidity: String?,
    val uvi: String?,
    val sunrise: String?,
    val sunset: String?,
    val cloud: String?
) : Parcelable {

    constructor(dailyWeather: DailyWeather) : this(
        dailyWeather.dt?.toDayString(),
        dailyWeather.dt?.toDayName(),
        dailyWeather.description,
        dailyWeather.icon,
        dailyWeather.max?.toInt().toString(),
        dailyWeather.min?.toInt().toString(),
        dailyWeather.average?.toInt().toString(),
        dailyWeather.windSpeed?.toInt().toString(),
        (dailyWeather.pop?.times(100))?.toInt().toString(),
        dailyWeather.humidity?.toString(),
        dailyWeather.uvi?.toInt().toString(),
        dailyWeather.sunrise?.toTime(),
        dailyWeather.sunset?.toTime(),
        dailyWeather.clouds?.toString()
    )
}