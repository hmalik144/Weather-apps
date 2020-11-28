package com.appttude.h_mal.atlas_weather.data.network.interceptors

import com.appttude.h_mal.atlas_weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Interceptor used to add default query parameters to api calls
 */
class QueryParamsInterceptor : Interceptor{

    val id = BuildConfig.ParamOne

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("appid", id)
                .build()

        // Request customization: add request headers
        // Request customization: add request headers
        val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}