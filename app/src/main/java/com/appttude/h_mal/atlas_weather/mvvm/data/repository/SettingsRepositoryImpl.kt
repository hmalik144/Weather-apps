package com.appttude.h_mal.atlas_weather.mvvm.data.repository

import com.appttude.h_mal.atlas_weather.mvvm.data.prefs.PreferenceProvider

class SettingsRepositoryImpl(
        val prefs: PreferenceProvider
) : SettingsRepository{

    override fun isNotificationsEnabled(): Boolean = prefs.isNotificationsEnabled()

    override fun setFirstTime() = prefs.setFirstTimeRun()
}