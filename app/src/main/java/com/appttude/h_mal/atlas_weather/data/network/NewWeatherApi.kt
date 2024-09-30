package com.appttude.h_mal.atlas_weather.data.network

import com.appttude.h_mal.atlas_weather.data.network.response.weather.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NewWeatherApi : Api {

    @GET("{location}")
    suspend fun getFromApi(
        @Query("contentType") exclude: String = "json",
        @Query("unitGroup") units: String = "uk",
        @Path("location") location: String
    ): Response<WeatherApiResponse>

}

