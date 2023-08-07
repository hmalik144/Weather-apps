package com.appttude.h_mal.atlas_weather.application

import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.NetworkModule
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase

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