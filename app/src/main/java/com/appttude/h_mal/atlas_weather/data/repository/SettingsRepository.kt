package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.model.types.UnitType

interface SettingsRepository {
    fun isNotificationsEnabled(): Boolean
    fun setFirstTime()
    fun getFirstTime(): Boolean
    fun isBlackBackground(): Boolean
    fun saveUnitType(unitType: UnitType)
    fun getUnitType(): UnitType
}