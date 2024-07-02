package com.appttude.h_mal.atlas_weather.ui.settings

import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.base.BasePreferencesFragment
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.viewmodel.SettingsViewModel

class SettingsFragment : BasePreferencesFragment<SettingsViewModel>(R.xml.prefs) {

    override fun preferenceChanged(key: String) {
        when (key) {

            "temp_units" -> viewModel.refreshWeatherData()
            "notif_boolean" -> {
                // TODO: update notification
//                viewModel.updateWidget()
//                displayToast("Widget background has been updates")
            }
        }
    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        if (data is String) displayToast(data)
    }
}

//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.prefs, rootKey)
//
//        //listener on changed sort order preference:
//        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        prefs.registerOnSharedPreferenceChangeListener { _, key ->
//            if (key == "temp_units") {
//
//            }
//            if (key == "notif_boolean") {
//                setupNotificationBroadcaster(requireContext())
//            }
//        }
//    }
//
//    fun setupNotificationBroadcaster(context: Context) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val notificationIntent = Intent(context, NotificationReceiver::class.java)
//        val broadcast = PendingIntent.getBroadcast(
//            context, 100, notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val cal: Calendar = Calendar.getInstance()
//        cal.set(Calendar.HOUR_OF_DAY, 6)
//        cal.set(Calendar.MINUTE, 8)
//        cal.set(Calendar.SECOND, 5)
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            cal.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
//            broadcast
//        )
//    }
//}