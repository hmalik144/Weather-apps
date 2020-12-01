package com.appttude.h_mal.atlas_weather.monoWeather.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return MyWidgetRemoteViewsFactory(applicationContext, intent)
    }
}