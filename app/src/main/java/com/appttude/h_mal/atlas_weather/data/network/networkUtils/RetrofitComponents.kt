package com.appttude.h_mal.atlas_weather.data.network.networkUtils

import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

fun buildOkHttpClient(
        vararg interceptor: Interceptor,
        timeoutSeconds: Long = 30L
): OkHttpClient {

    val builder = OkHttpClient.Builder()

    interceptor.forEach {
        if (it is NetworkInterceptor) {
            builder.addNetworkInterceptor(it)
        } else {
            builder.addInterceptor(it)
        }
    }

    builder.connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)

    return builder.build()
}

fun <T> createRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        service: Class<T>
): T {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
}