package com.appttude.h_mal.application

import com.appttude.h_mal.data.location.LocationProviderImpl
import com.appttude.h_mal.data.network.NetworkModule
import com.appttude.h_mal.data.network.WeatherApi
import com.appttude.h_mal.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.data.room.AppDatabase

const val LOCATION_PERMISSION_REQUEST = 505

class AppClass : BaseAppClass() {

    override fun createNetworkModule(): WeatherApi {
        return NetworkModule().invoke<WeatherApi>(
                NetworkConnectionInterceptor(this),
                QueryParamsInterceptor(),
                loggingInterceptor
        ) as WeatherApi
    }

    override fun createLocationModule() = LocationProviderImpl(this)

    override fun createRoomDatabase(): AppDatabase = AppDatabase(this)

}