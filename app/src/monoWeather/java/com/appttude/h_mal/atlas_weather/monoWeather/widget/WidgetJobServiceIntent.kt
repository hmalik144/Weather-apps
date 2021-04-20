package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.widget.WidgetData
import com.appttude.h_mal.atlas_weather.monoWeather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.isInternetAvailable
import com.appttude.h_mal.atlas_weather.utils.tryOrNullSuspended
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Example implementation of a JobIntentService.
 */
class WidgetJobServiceIntent : BaseWidgetServiceIntentClass() {

    override fun onHandleWork(intent: Intent) {
        setKodein(this)
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val thisAppWidget = ComponentName(packageName, NewAppWidget::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || !isInternetAvailable(this.applicationContext)) {
            for (appWidgetId in appWidgetIds) {
                setEmptyView(this, appWidgetManager, appWidgetId)
            }
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                val result = getWidgetWeather()

                for (appWidgetId in appWidgetIds) {
                    bindView(this@WidgetJobServiceIntent, appWidgetManager, appWidgetId, result)
                }
            }
        }
    }

    private fun createForecastListIntent(
            context: Context,
            appWidgetId: Int
    ): Intent {
        return Intent(context, WidgetRemoteViewsService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getWidgetWeather(): WidgetData? {
        return tryOrNullSuspended {
            helper.fetchData()
            helper.getWidgetWeather()
        }

    }

    private fun setEmptyView(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        try {
            val error = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                "No Permission"
            } else if (!isInternetAvailable(context.applicationContext)) {
                "No Connection"
            } else {
                "No Data"
            }
            val updatePendingIntent = createUpdatePendingIntent(NewAppWidget::class.java, context, appWidgetId)
            val views = createRemoteView(context, R.layout.weather_app_widget)
            bindEmptyView(appWidgetManager, appWidgetId, views, updatePendingIntent, error)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun bindEmptyView(
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            views: RemoteViews,
            clickingUpdateIntent: PendingIntent?,
            warning: String
    ) {
        setLastUpdated(views)
        views.setTextViewText(R.id.widget_current_location, warning)
        views.setImageViewResource(R.id.widget_current_icon, R.drawable.ic_baseline_cloud_off_24)
        views.setImageViewResource(R.id.location_icon, 0)

        views.setTextViewText(R.id.widget_main_temp, "")
        views.setTextViewText(R.id.widget_feel_temp, "")

        views.setOnClickPendingIntent(R.id.widget_current_icon, clickingUpdateIntent)
        views.setOnClickPendingIntent(R.id.widget_current_location, clickingUpdateIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview)
    }

    private fun bindView(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            weather: WidgetData?) {
        val views = createRemoteView(context, R.layout.weather_app_widget)
        setLastUpdated(views)
        views.setInt(R.id.whole_widget_view, "setBackgroundColor", helper.getWidgetBackground())
        val clickingUpdatePendingIntent = createUpdatePendingIntent(NewAppWidget::class.java, context, appWidgetId)
        val forecastListIntent = createForecastListIntent(context, appWidgetId)

        if (weather != null) {
            val clickPendingIntentTemplate =
                    createClickingPendingIntent(context, MainActivity::class.java)

            views.apply {
                setTextViewText(R.id.widget_main_temp, weather.currentTemp)
                setTextViewText(R.id.widget_feel_temp, "°C")
                setTextViewText(R.id.widget_current_location, weather.location)
                setImageViewResource(R.id.location_icon, R.drawable.location_flag)
                CoroutineScope(Dispatchers.Main).launch {
                    Picasso.get().load(weather.icon).into(views, R.id.widget_current_icon, intArrayOf(appWidgetId))
                }
                setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate)
                setOnClickPendingIntent(R.id.widget_current_icon, clickingUpdatePendingIntent)
                setOnClickPendingIntent(R.id.widget_current_location, clickingUpdatePendingIntent)

                setRemoteAdapter(R.id.widget_listview, forecastListIntent)
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview)
        } else {
            bindEmptyView(appWidgetManager, appWidgetId, views, clickingUpdatePendingIntent, "No Connection")
        }

    }

    private fun setLastUpdated(views: RemoteViews){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formatted = current.format(formatter)
            views.setTextViewText(R.id.widget_current_status, "last updated: $formatted")
        }
    }

    companion object {
        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1000

        /**
         * Convenience method for enqueuing work in to this service.
         */
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, WidgetJobServiceIntent::class.java, JOB_ID, work)
        }
    }
}