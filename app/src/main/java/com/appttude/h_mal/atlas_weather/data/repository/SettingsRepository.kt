package com.appttude.h_mal.atlas_weather.data.repository

interface SettingsRepository {
    fun isNotificationsEnabled(): Boolean
    fun setFirstTime()
    fun getFirstTime(): Boolean
    fun isBlackBackground(): Boolean
}