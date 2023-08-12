package com.appttude.h_mal.atlas_weather.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import retrofit2.Response


open class BaseTest {
    private val gson by lazy { Gson() }

    fun <T : Any> getTestData(resourceName: String): T {
        val json = this::class.java.classLoader!!.getResource(resourceName).readText()
        val typeToken = object : TypeToken<T>() {}.type
        return gson.fromJson<T>(json, typeToken)
    }

    fun <T : Any> getTestData(resourceName: String, cls: Class<T>): T {
        val json = this::class.java.classLoader!!.getResource(resourceName).readText()
        return gson.fromJson<T>(json, cls)
    }

    inline fun <reified T : Any> createSuccessfulRetrofitMock(): Response<T> {
        val mockResponse = mockk<T>()
        return Response.success(mockResponse)
    }

    fun <T: Any> createErrorRetrofitMock(code: Int = 400): Response<T> {
        val responseBody = mockk<ResponseBody>(relaxed = true)
        val rawResponse = mockk<okhttp3.Response>().also {
            every { it.code } returns code
        }
        return Response.error<T>(code, responseBody)
    }
}