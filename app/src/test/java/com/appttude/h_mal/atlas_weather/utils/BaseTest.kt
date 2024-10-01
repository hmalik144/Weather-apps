package com.appttude.h_mal.atlas_weather.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
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
        return Response.error<T>(code, responseBody)
    }

    fun <T: Any> createErrorRetrofitMock(errorMessage: String, code: Int = 400): Response<T> {
        val responseBody = errorMessage.toResponseBody("application/json".toMediaType())
        val rawResponse = mockk<okhttp3.Response>(relaxed = true).also {
            every { it.code } returns code
            every { it.isSuccessful } returns false
            every { it.body } returns responseBody
            every { it.message } returns errorMessage
        }
        return Response.error<T>(responseBody, rawResponse)
    }
}