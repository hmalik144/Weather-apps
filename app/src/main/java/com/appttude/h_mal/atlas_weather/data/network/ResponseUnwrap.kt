package com.appttude.h_mal.atlas_weather.data.network

import retrofit2.HttpException
import retrofit2.Response

abstract class ResponseUnwrap {

    suspend fun <T : Any> responseUnwrap(
        call: suspend () -> Response<T>
    ): T {

        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw HttpException(response)
        }
    }
}