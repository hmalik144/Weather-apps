package com.appttude.h_mal.data.repository

import com.appttude.h_mal.data.prefs.PreferenceProvider

class SettingsRepositoryImpl(
        private val prefs: PreferenceProvider
) : SettingsRepository{

    override fun isNotificationsEnabled(): Boolean = prefs.isNotificationsEnabled()

    override fun setFirstTime() = prefs.setFirstTimeRun()

    override fun isBlackBackground() = prefs.isWidgetBlackground()
}