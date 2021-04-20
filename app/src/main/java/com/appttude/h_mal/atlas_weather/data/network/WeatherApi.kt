package com.appttude.h_mal.atlas_weather.data.network

import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.buildOkHttpClient
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.createRetrofit
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("onecall?")
    suspend fun getFromApi(
            @Query("lat") query: String,
            @Query("lon") lon: String,
            @Query("exclude") exclude: String = "minutely",
            @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    // invoke method creating an invocation of the api call
    companion object{
        operator fun invoke(
                baseUrl: String,
                networkConnectionInterceptor: NetworkConnectionInterceptor,
                queryParamsInterceptor: QueryParamsInterceptor,
                loggingInterceptor: HttpLoggingInterceptor
        ) : WeatherApi {

            val okHttpClient = buildOkHttpClient(
                    networkConnectionInterceptor,
                    queryParamsInterceptor,
                    loggingInterceptor
            )

            return createRetrofit(
                    baseUrl,
                    okHttpClient,
                    WeatherApi::class.java
            )
        }
    }
}

