package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.app.JobIntentService
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

abstract class BaseWidgetServiceIntentClass<T: AppWidgetProvider> : JobIntentService(){

    lateinit var appWidgetManager: AppWidgetManager
    lateinit var appWidgetIds: IntArray

    fun initBaseWidget(componentName: ComponentName){
        appWidgetManager = AppWidgetManager.getInstance(baseContext)
        appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
    }

    fun createRemoteView(@LayoutRes id: Int): RemoteViews {
        return RemoteViews(packageName, id)
    }

    // Create pending intent commonly used for 'click to update' features
    fun createUpdatePendingIntent(
            appWidgetProvider: Class<T>,
            appWidgetId: Int
    ): PendingIntent? {
        val seconds = (System.currentTimeMillis() / 1000L).toInt()
        val intentUpdate = Intent(applicationContext, appWidgetProvider)
        intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val idArray = intArrayOf(appWidgetId)
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)
        return PendingIntent.getBroadcast(
                this, seconds, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * create a pending intent used to navigate to activity:
     * @param activityClass
     */
    fun <T: Activity> createClickingPendingIntent(activityClass: Class<T>): PendingIntent {
        val clickIntentTemplate = Intent(this, activityClass)

        return TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun setImageView(
            path: String?,
            views: RemoteViews,
            @IdRes viewId: Int,
            appWidgetId: Int
    ){
        CoroutineScope(Dispatchers.Main).launch {
            Picasso.get().load(path).into(views, viewId, intArrayOf(appWidgetId))
        }
    }

    open fun bindView(widgetId: Int, views: RemoteViews, data: Any?) {}
    open fun bindEmptyView(widgetId: Int, views: RemoteViews, data: Any?) {}
    open fun bindErrorView(widgetId: Int, views: RemoteViews, data: Any?) {}
}