package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent

fun <T: Activity> createClickingPendingIntent(context: Context, activityClass: Class<T>): PendingIntent {
    val clickIntentTemplate = Intent(context, activityClass)

    return TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(clickIntentTemplate)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
}