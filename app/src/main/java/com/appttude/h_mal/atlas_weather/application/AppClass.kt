package com.appttude.h_mal.atlas_weather.application

import androidx.room.RoomDatabase
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.Api
import com.appttude.h_mal.atlas_weather.data.network.NetworkModule
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.repository.RepositoryImpl
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepositoryImpl
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

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