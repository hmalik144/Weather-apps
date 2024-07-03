package com.appttude.h_mal.atlas_weather.application


import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun getFlavourModule(application: Application) = FlavourModule(application).build()
class FlavourModule(val application: Application) {
    fun build() = Kodein.Module("Flavour") {
        bind() from provider {
            ApplicationViewModelFactory(
                application,
                instance(),
                instance(),
                instance(),
            )
        }
    }
}