package com.appttude.h_mal.data.network.interceptors

import android.content.Context
import com.appttude.h_mal.utils.isInternetAvailable
import okhttp3.Interceptor
import java.io.IOException

class NetworkConnectionInterceptor(
    context: Context
) : NetworkInterceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if (!isInternetAvailable(applicationContext)){
            throw IOException("Make sure you have an active data connection")
        }
        return chain.proceed(chain.request())
    }

}