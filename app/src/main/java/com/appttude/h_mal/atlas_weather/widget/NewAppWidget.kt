package com.appttude.h_mal.atlas_weather.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.appttude.h_mal.atlas_weather.widget.WidgetJobServiceIntent.Companion.enqueueWork

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        loadWidget(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        loadWidget(context)
    }

    override fun onDisabled(context: Context) { }

    private fun loadWidget(context: Context){
        val mIntent = Intent(context, WidgetJobServiceIntent::class.java)
        enqueueWork(context, mIntent)
    }
}