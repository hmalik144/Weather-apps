package com.appttude.h_mal.atlas_weather.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.notification.NotificationReceiver
import com.appttude.h_mal.atlas_weather.widget.NewAppWidget
import java.util.Calendar

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)

        //listener on changed sort order preference:
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "temp_units") {
                val intent = Intent(requireContext(), NewAppWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(requireContext())
                    .getAppWidgetIds(ComponentName(requireContext(), NewAppWidget::class.java))
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                requireContext().sendBroadcast(intent)
            }
            if (key == "notif_boolean") {
                setupNotificationBroadcaster(requireContext())
            }

            if (key == "widget_black_background"){
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                val widgetManager = AppWidgetManager.getInstance(requireContext())
                val ids =
                    widgetManager.getAppWidgetIds(ComponentName(requireContext(), NewAppWidget::class.java))
                AppWidgetManager.getInstance(requireContext())
                    .notifyAppWidgetViewDataChanged(ids, R.id.whole_widget_view)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                requireContext().sendBroadcast(intent)
            }
        }
    }

    fun setupNotificationBroadcaster(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 6)
        cal.set(Calendar.MINUTE, 8)
        cal.set(Calendar.SECOND, 5)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, broadcast)
    }
}