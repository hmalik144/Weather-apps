package com.appttude.h_mal.atlas_weather.data.network.response.weather

import com.appttude.h_mal.atlas_weather.model.DataMapper
import com.appttude.h_mal.atlas_weather.model.weather.Current
import com.appttude.h_mal.atlas_weather.model.weather.DailyWeather
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.google.gson.annotations.SerializedName
import com.appttude.h_mal.atlas_weather.model.weather.Hour as FullWeatherHour


data class WeatherApiResponse(
    @SerializedName("queryCost") var queryCost: Int? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("resolvedAddress") var resolvedAddress: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("tzoffset") var tzoffset: Int? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("days") var days: ArrayList<Days> = arrayListOf(),
    @SerializedName("alerts") var alerts: ArrayList<Alerts> = arrayListOf(),
    @SerializedName("currentConditions") var currentConditions: CurrentConditions? = CurrentConditions()
): DataMapper<FullWeather> {

    override fun mapData(): FullWeather {
        val hours = mutableListOf(days[0].hours).apply { add(days[1].hours) }.flatten().subList(0,23).map { FullWeatherHour(it) }.toList()
        val collectedDays = mutableListOf(days.subList(0,7)).flatten().map { DailyWeather(it) }.toList()
        return FullWeather(
            current = Current(currentConditions),
            timezone = timezone,
            timezoneOffset = tzoffset,
            hourly = hours,
            daily = collectedDays,
            lat = latitude,
            lon = longitude,
            locationString = address,
            temperatureUnit = null
        )
    }
}