package com.appttude.h_mal.data.network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class ResponseUnwrap {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun <T : Any> responseUnwrap(
            call: suspend () -> Response<T>
    ): T {

        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val errorMessage = error?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    null
                }
            } ?: "Error Code: ${response.code()}"

            throw IOException(errorMessage)
        }
    }
}