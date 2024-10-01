package com.appttude.h_mal.atlas_weather.application

import android.app.Application
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.network.Api
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.repository.RepositoryImpl
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepositoryImpl
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

abstract class BaseAppClass : Application(), KodeinAware {

    // Kodein aware to initialise the classes used for DI
    override val kodein = Kodein.lazy {
        import(parentModule)
        import(getFlavourModule(application = this@BaseAppClass))
    }

    val parentModule = Kodein.Module("Parent Module", allowSilentOverride = true) {
        import(androidXModule(this@BaseAppClass))

        bind() from singleton { createNetworkModule() as WeatherApi }
        bind() from singleton { createLocationModule() }

        bind() from singleton { Gson() }
        bind() from singleton { createRoomDatabase() }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { RepositoryImpl(instance(), instance(), instance()) }
        bind() from singleton { SettingsRepositoryImpl(instance()) }
        bind() from singleton { ServicesHelper(instance(), instance(), instance()) }
        bind() from singleton { WeatherSource(instance(), instance()) }
    }

    abstract fun createNetworkModule(): Api
    abstract fun createLocationModule(): LocationProvider
    abstract fun createRoomDatabase(): AppDatabase

}