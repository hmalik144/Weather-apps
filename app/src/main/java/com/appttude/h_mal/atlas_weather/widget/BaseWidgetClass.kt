package com.appttude.h_mal.atlas_weather.widget

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.LayoutRes

abstract class BaseWidgetClass : AppWidgetProvider(){

    fun createRemoteView(context: Context, @LayoutRes id: Int): RemoteViews {
        return RemoteViews(context.packageName, id)
    }

    fun createUpdatePendingIntent(context: Context, appWidgetId: Int): PendingIntent? {
        val seconds = (System.currentTimeMillis() / 1000L).toInt()
        val intentUpdate = Intent(context, this::class.java)
        intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val idArray = intArrayOf(appWidgetId)
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)
        return PendingIntent.getBroadcast(
                context, seconds, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun <T: Activity> createClickingPendingIntent(context: Context, activityClass: Class<T>): PendingIntent {
        val clickIntentTemplate = Intent(context, activityClass)

        return TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}