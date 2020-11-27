package com.appttude.h_mal.atlas_weather.legacy.data.sql;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ForecastContract {

    public ForecastContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.appttude.h_mal.atlas_weather";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FORECASTS = "forecasts";
    public static final String PATH_FORECASTS_WIDGET = "widgetitems";

    public static final class ForecastEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FORECASTS);

        public static final Uri CONTENT_URI_WIDGET = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FORECASTS_WIDGET);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECASTS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECASTS;


        public final static String TABLE_NAME = "forecasts";

        public final static String TABLE_NAME_WIDGET = "widgetitems";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FORECAST_NAME = "name";

        public final static String COLUMN_WIDGET_FORECAST_ITEM = "widgetforcastitem";

        }
    }
