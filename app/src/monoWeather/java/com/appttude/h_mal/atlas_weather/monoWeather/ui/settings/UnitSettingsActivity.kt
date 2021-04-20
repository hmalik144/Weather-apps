package com.appttude.h_mal.atlas_weather.monoWeather.ui.settings

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.monoWeather.widget.NewAppWidget


class UnitSettingsActivity : PreferenceActivity() {
    private var prefListener: OnSharedPreferenceChangeListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.prefs_screen, false)
        fragmentManager.beginTransaction().replace(android.R.id.content, MyPreferenceFragment()).commit()

        //listener on changed sort order preference:
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        prefListener = OnSharedPreferenceChangeListener { _, key ->
            if (key == "temp_units") {
                val intent = Intent(baseContext, NewAppWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, NewAppWidget::class.java))
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)
            }

            if (key == "widget_black_background"){
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                val widgetManager = AppWidgetManager.getInstance(this)
                val ids = widgetManager.getAppWidgetIds(ComponentName(this, NewAppWidget::class.java))
                AppWidgetManager.getInstance(this).notifyAppWidgetViewDataChanged(ids, R.id.whole_widget_view)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }


    class MyPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.prefs_screen)
        }
    }
}

