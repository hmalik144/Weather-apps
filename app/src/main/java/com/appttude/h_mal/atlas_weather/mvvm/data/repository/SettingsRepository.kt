package com.appttude.h_mal.atlas_weather.mvvm.data.repository

interface SettingsRepository {
    fun isNotificationsEnabled(): Boolean
    fun setFirstTime()
}