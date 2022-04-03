package com.appttude.h_mal.atlas_weather.data.network

import com.appttude.h_mal.atlas_weather.data.network.networkUtils.buildOkHttpClient
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.createRetrofit
import okhttp3.Interceptor

open class BaseNetworkModule {
    // Declare the method we want/can change (no annotations)
    open fun baseUrl() = "/"

    inline fun <reified T: Api> invoke(
            vararg interceptors: Interceptor
    ): Api {

        val okHttpClient = buildOkHttpClient(*interceptors)

        return createRetrofit(
                baseUrl(),
                okHttpClient,
                T::class.java
        )
    }
}