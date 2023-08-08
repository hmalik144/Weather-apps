package com.appttude.h_mal.atlas_weather.data.network.interceptors

import androidx.test.espresso.idling.CountingIdlingResource
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockingNetworkInterceptor(
    private val idlingResource: CountingIdlingResource
) : Interceptor {

    private var feedMap: MutableMap<String, String> = mutableMapOf()
    private var urlCallTracker: MutableMap<String, Int> = mutableMapOf()

    override fun intercept(chain: Interceptor.Chain): Response {
        idlingResource.increment()
        val original = chain.request()
        val originalHttpUrl = original.url.toString().split("?")[0]

        urlCallTracker.computeIfPresent(originalHttpUrl) { _, j ->
            j + 1
        }

        feedMap[originalHttpUrl]?.let { jsonPath ->
            val body = jsonPath.toResponseBody("application/json".toMediaType())

            val chainResponseBuilder = Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .request(original)
                .message("OK")
                .body(body)
            idlingResource.decrement()
            return chainResponseBuilder.build()
        }

        idlingResource.decrement()
        return chain.proceed(original)
    }

    fun addUrlStub(url: String, data: String) = feedMap.put(url, data)
    fun removeUrlStub(url: String) = feedMap.remove(url)

}