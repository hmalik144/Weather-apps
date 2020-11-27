package com.appttude.h_mal.atlas_weather.mvvm.data.network

import com.appttude.h_mal.atlas_weather.mvvm.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.mvvm.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.mvvm.data.network.response.forecast.WeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("onecall?")
    suspend fun getFromApi(
            @Query("lat") query: String,
            @Query("lon") lon: String,
            @Query("exclude") exclude: String = "hourly,minutely",
            @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    // invoke method creating an invocation of the api call
    companion object{
        operator fun invoke(
            // injected @params
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            queryParamsInterceptor: QueryParamsInterceptor
        ) : WeatherApi {


            val baseUrl = "https://api.openweathermap.org/data/2.5/"

            // okHttpClient with interceptors
            val okkHttpclient = OkHttpClient.Builder()
                .addNetworkInterceptor(networkConnectionInterceptor)
                    .addInterceptor(queryParamsInterceptor)
                .build()

            // creation of retrofit class
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}

