package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetData
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance


class MyWidgetRemoteViewsFactory(
        private val context: Context,
        val intent: Intent
) : RemoteViewsFactory{
    private val TAG = "MyWidgetRemoteViewsFactory"

    private val kodein = LateInitKodein()
    private val helper : ServicesHelper by kodein.instance()

    private var appWidgetId: Int? = 0
    private var list: List<InnerWidgetData>? = null

    init {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        kodein.baseKodein = (context.applicationContext as KodeinAware).kodein

    }

    override fun onCreate() {}
    override fun onDataSetChanged() {
        runBlocking {
            list = helper.getWidgetInnerWeather()
        }
    }
    override fun onDestroy() {}

    override fun getCount(): Int = list?.size ?: 5

    override fun getViewAt(i: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        if (list.isNullOrEmpty()) return rv


        list?.get(i)?.let {
            rv.setTextViewText(R.id.widget_item_day, it.date)
            rv.setImageViewBitmap(R.id.widget_item_image, it.icon)
            rv.setTextViewText(R.id.widget_item_temp_high, it.highTemp)
            rv.setOnClickFillInIntent(R.id.widget_item_layout, intent)
        }

        return rv
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item_loading)
    }

    override fun getViewTypeCount(): Int = 1


    override fun getItemId(i: Int): Long = i.toLong()


    override fun hasStableIds(): Boolean {
        return true
    }


}