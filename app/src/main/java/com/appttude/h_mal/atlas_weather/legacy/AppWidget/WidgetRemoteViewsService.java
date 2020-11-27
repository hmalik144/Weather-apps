package com.appttude.h_mal.atlas_weather.legacy.AppWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class WidgetRemoteViewsService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewsFactory(getApplicationContext(),intent);
    }
}
