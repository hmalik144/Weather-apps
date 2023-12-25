package com.appttude.h_mal.atlas_weather.viewmodel

import android.Manifest
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.application.AppClass
import com.appttude.h_mal.atlas_weather.base.baseViewModels.BaseAndroidViewModel
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.widget.NewAppWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(
    application: Application,
    private val locationProvider: LocationProvider,
    private val weatherSource: WeatherSource,
    private val settingsRepository: SettingsRepository
) : BaseAndroidViewModel(application) {

    private fun getContext() = getApplication<AppClass>().applicationContext

    fun updateWidget() {
        val context = getContext()
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val widgetManager = AppWidgetManager.getInstance(context)
        val ids =
            widgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    NewAppWidget::class.java
                )
            )
        AppWidgetManager.getInstance(context)
            .notifyAppWidgetViewDataChanged(ids, R.id.whole_widget_view)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }

    fun refreshWeatherData() {
        onStart()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                if (ActivityCompat.checkSelfPermission(
                        getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Get location
                    val latLong = locationProvider.getCurrentLatLong()
                    weatherSource.forceFetchWeather(latLong)
                }
                updateWidget()
                val units = settingsRepository.getUnitType().name.lowercase(Locale.ROOT)
                onSuccess("Units have been changes to $units")
            } catch (e: Exception) {
                onError(e.message ?: "Retrieving weather failed")
            }
        }
    }

}