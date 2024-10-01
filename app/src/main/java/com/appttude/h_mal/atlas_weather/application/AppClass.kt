package com.appttude.h_mal.atlas_weather.application

import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.NetworkModule
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase

open class AppClass : BaseAppClass() {

    override fun createNetworkModule(): WeatherApi {
        return NetworkModule().invoke<WeatherApi>(
            NetworkConnectionInterceptor(this),
            QueryParamsInterceptor(),
            loggingInterceptor
        ) as WeatherApi
    }

    override fun createLocationModule(): LocationProvider = LocationProviderImpl(this)

    override fun createRoomDatabase(): AppDatabase = AppDatabase(this)

}