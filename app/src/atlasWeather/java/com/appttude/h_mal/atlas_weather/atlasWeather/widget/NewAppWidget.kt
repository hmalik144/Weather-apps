package com.appttude.h_mal.atlas_weather.atlasWeather.widget

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.atlasWeather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.model.widget.WidgetData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

/**
 * Implementation of App Widget functionality.
 */
private val TAG = NewAppWidget::class.java.simpleName
class NewAppWidget : BaseWidgetClass() {

    private val kodein = LateInitKodein()
    private val helper : ServicesHelper by kodein.instance()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        kodein.baseKodein = (context.applicationContext as KodeinAware).kodein
        // There may be multiple widgets active, so update all of them
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val results = helper.fetchData()
            if (results) return@launch
            val weatherWidgetCurrent = helper.getWidgetWeather()

            withContext(Dispatchers.Main){
                for (appWidgetId in appWidgetIds) {
                    val updatePendingIntent = createUpdatePendingIntent(context, appWidgetId)
                    val views = createRemoteView(context, R.layout.new_app_widget)
                    bindView(context, appWidgetId, views, updatePendingIntent, weatherWidgetCurrent)
                }
                super.onUpdate(context, appWidgetManager, appWidgetIds)
            }

        }
    }

    override fun onEnabled(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, NewAppWidget::class.java.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview)
        } catch (e: Exception) {
            Log.e(TAG, "onEnabled: ", e)
        }
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action ==
                AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, NewAppWidget::class.java.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview)
        }
        super.onReceive(context, intent)
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

    private fun bindView(
            context: Context,
            appWidgetId: Int,
            views: RemoteViews,
            updatePendingIntent: PendingIntent?,
            weather: WidgetData?){

        val appWidgetManager = AppWidgetManager.getInstance(context)

        views.setInt(R.id.whole_widget_view, "setBackgroundColor", helper.getWidgetBackground())

        weather?.let {

            val intent = createForecastListIntent(
                    context,
                    appWidgetId
            )

            views.setRemoteAdapter(R.id.widget_listview, intent)
            views.setTextViewText(R.id.widget_main_temp, it.currentTemp)
            views.setTextViewText(R.id.widget_feel_temp, "°C")
            views.setTextViewText(R.id.widget_current_location, it.location)
            views.setImageViewResource(R.id.location_icon, R.drawable.location_flag)
            views.setImageViewBitmap(R.id.widget_current_icon, it.icon)

            val clickPendingIntentTemplate = createClickingPendingIntent(context, MainActivity::class.java)
            views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate)

            views.setOnClickPendingIntent(R.id.widget_current_icon, updatePendingIntent)
            views.setOnClickPendingIntent(R.id.widget_current_location, updatePendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview)
            return
        }

        Log.i(TAG, "onPostExecute: weather is empty")
        views.setTextViewText(R.id.widget_current_location, "Refresh")
        views.setImageViewResource(R.id.widget_current_icon, R.drawable.widget_error_icon)
        views.setImageViewResource(R.id.location_icon, R.drawable.refreshing)
        views.setOnClickPendingIntent(R.id.widget_current_icon, updatePendingIntent)
        views.setOnClickPendingIntent(R.id.widget_current_location, updatePendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}