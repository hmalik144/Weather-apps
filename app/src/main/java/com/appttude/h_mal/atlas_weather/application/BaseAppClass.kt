package com.appttude.h_mal.atlas_weather.application

import android.app.Application
import androidx.test.espresso.idling.CountingIdlingResource
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
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

abstract class BaseAppClass : Application(), KodeinAware {

    // Kodein creation of modules to be retrieve within the app
    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseAppClass))

        bind() from singleton { createNetworkModule() as WeatherApi}
        bind() from singleton { createLocationModule() }

        bind() from singleton { Gson() }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { RepositoryImpl(instance(), instance(), instance()) }
        bind() from singleton { SettingsRepositoryImpl(instance()) }
        bind() from singleton { ServicesHelper(instance(), instance(), instance()) }
        bind() from provider { ApplicationViewModelFactory(instance(), instance()) }
    }

    abstract fun createNetworkModule() : Api
    abstract fun createLocationModule() : LocationProvider

}