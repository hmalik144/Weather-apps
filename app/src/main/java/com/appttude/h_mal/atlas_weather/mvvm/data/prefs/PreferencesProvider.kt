package com.appttude.h_mal.atlas_weather.mvvm.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.mvvm.data.room.entity.CURRENT_LOCATION

/**
 * Shared preferences to save & load last timestamp
 */
const val LOCATION_CONST = "location_"
class PreferenceProvider(
    context: Context
){

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveLastSavedAt(locationName: String) {
        preference.edit().putLong(
                locationName,
                System.currentTimeMillis()
        ).apply()
    }

    fun getLastSavedAt(locationName: String): Long? {
        return  preference.getLong(locationName, 0L)
    }

    fun getAllKeys() = preference.all.keys.apply {
        remove(CURRENT_LOCATION)
    }

    fun deleteLocation(locationName: String){
        preference.edit().remove(locationName).apply()
    }

    fun isNotificationsEnabled(): Boolean = preference.getBoolean("notif_boolean", true)

    fun setFirstTimeRun(){
        preference.edit().putBoolean("FIRST_TIME_RUN", false).apply()
    }

}