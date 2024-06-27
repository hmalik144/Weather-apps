package com.appttude.h_mal.atlas_weather.application

import com.appttude.h_mal.atlas_weather.service.notification.NotificationHelper
import com.appttude.h_mal.atlas_weather.service.notification.NotificationService
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


open class AtlasApp : AppClass() {

    private lateinit var notificationService: NotificationService

    override val flavourModule = super.flavourModule.copy {
        bind() from singleton {
            NotificationHelper(
                instance(),
                instance(),
            )
        }

        bind() from singleton {
            NotificationService(this@AtlasApp).apply { notificationService = this }
        }

        bind() from provider {
            ApplicationViewModelFactory(
                this@AtlasApp,
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }

//    override fun onCreate() {
//        super.onCreate()
//        notificationService.schedulePushNotifications()
//    }

    fun scheduleNotifications() = notificationService.schedulePushNotifications()

    fun unscheduleNotifications() = notificationService.unschedulePushNotifications()
}