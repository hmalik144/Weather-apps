package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.model.types.UnitType

class SettingsRepositoryImpl(
    private val prefs: PreferenceProvider
) : SettingsRepository {

    override fun isNotificationsEnabled(): Boolean = prefs.isNotificationsEnabled()

    override fun setFirstTime() = prefs.setFirstTimeRun()
    override fun getFirstTime(): Boolean = prefs.getFirstTimeRun()

    override fun isBlackBackground() = prefs.isWidgetBackground()


    override fun saveUnitType(unitType: UnitType) {
        prefs.setUnitsType(unitType)
    }

    override fun getUnitType(): UnitType {
        return prefs.getUnitsType()
    }
}