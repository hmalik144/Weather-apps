package com.appttude.h_mal.monoWeather.ui.settings

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.widget.NewAppWidget

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs_screen, rootKey)

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

            if (key == "widget_black_background") {
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
}