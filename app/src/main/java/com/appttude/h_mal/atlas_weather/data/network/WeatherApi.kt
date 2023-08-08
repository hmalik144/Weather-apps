package com.appttude.h_mal.atlas_weather.data.network

import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi : Api {

    @GET("onecall?")
    suspend fun getFromApi(
        @Query("lat") query: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

}

