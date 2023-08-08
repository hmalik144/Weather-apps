package com.appttude.h_mal.atlas_weather.data.network.response.forecast

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @field:SerializedName("current")
    val current: Current? = null,

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("timezone_offset")
    val timezoneOffset: Int? = null,

    @field:SerializedName("hourly")
    val hourly: List<Hour>? = null,

    @field:SerializedName("daily")
    val daily: List<DailyItem>? = null,

    @field:SerializedName("lon")
    val lon: Double = 0.00,

    @field:SerializedName("lat")
    val lat: Double = 0.00
)

