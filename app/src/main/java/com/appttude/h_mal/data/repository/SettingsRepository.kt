package com.appttude.h_mal.data.repository

interface SettingsRepository {
    fun isNotificationsEnabled(): Boolean
    fun setFirstTime()
    fun isBlackBackground(): Boolean
}