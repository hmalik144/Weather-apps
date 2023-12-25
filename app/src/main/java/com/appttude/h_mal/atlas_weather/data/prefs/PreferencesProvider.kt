package com.appttude.h_mal.atlas_weather.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.types.UnitType

/**
 * Shared preferences to save & load last timestamp
 */
const val LOCATION_CONST = "location_"
const val UNIT_CONST = "UnitType"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveLastSavedAt(locationName: String) {
        preference.edit().putLong(
            locationName,
            System.currentTimeMillis()
        ).apply()
    }

    fun getLastSavedAt(locationName: String): Long {
        return preference.getLong(locationName, 0L)
    }

    fun getAllKeysExcludingCurrent() = preference.all.keys.apply {
        remove(CURRENT_LOCATION)
    }

    fun deleteLocation(locationName: String) {
        preference.edit().remove(locationName).apply()
    }

    fun isNotificationsEnabled(): Boolean = preference.getBoolean("notif_boolean", true)

    fun setFirstTimeRun() {
        preference.edit().putBoolean("FIRST_TIME_RUN", false).apply()
    }

    fun getFirstTimeRun() = preference.getBoolean("FIRST_TIME_RUN", false)

    fun isWidgetBackground(): Boolean {
        return preference.getBoolean("widget_black_background", false)
    }

    fun setUnitsType(type: UnitType) {
        preference.edit().putString(UNIT_CONST, type.name).apply()
    }

    fun getUnitsType(): UnitType {
        val unit = preference.getString(UNIT_CONST, UnitType.METRIC.name)
        return UnitType.getByName(unit) ?: UnitType.METRIC
    }

}