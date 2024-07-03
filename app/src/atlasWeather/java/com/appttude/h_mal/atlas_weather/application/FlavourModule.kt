package com.appttude.h_mal.atlas_weather.application


import android.app.Application
import com.appttude.h_mal.atlas_weather.service.notification.NotificationHelper
import com.appttude.h_mal.atlas_weather.service.notification.NotificationService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun getFlavourModule(application: Application) = FlavourModule(application).build()
class FlavourModule(val application: Application) {
    fun build() = Kodein.Module("Flavour") {
        bind() from singleton {
            NotificationHelper(
                instance(),
                instance(),
            )
        }

        bind() from singleton {
            NotificationService(application)
        }

        bind() from provider {
            ApplicationViewModelFactory(
                application,
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }
}