package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.core.app.JobIntentService
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

abstract class BaseWidgetServiceIntentClass : JobIntentService(){

    private val kodein = LateInitKodein()
    val helper: ServicesHelper by kodein.instance()

    fun setKodein(context: Context){
        kodein.baseKodein = (context.applicationContext as KodeinAware).kodein
    }

    fun createRemoteView(context: Context, @LayoutRes id: Int): RemoteViews {
        return RemoteViews(context.packageName, id)
    }

    // Create pending intent commonly used for 'click to update' features
    fun <T: AppWidgetProvider> createUpdatePendingIntent(
            appWidgetProvider: Class<T>,
            context: Context,
            appWidgetId: Int
    ): PendingIntent? {
        val seconds = (System.currentTimeMillis() / 1000L).toInt()
        val intentUpdate = Intent(context.applicationContext, appWidgetProvider)
        intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val idArray = intArrayOf(appWidgetId)
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)
        return PendingIntent.getBroadcast(
                context, seconds, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * create a pending intent used to navigate to activity:
     * @param activityClass
     */
    fun <T: Activity> createClickingPendingIntent(context: Context, activityClass: Class<T>): PendingIntent {
        val clickIntentTemplate = Intent(context, activityClass)

        return TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}