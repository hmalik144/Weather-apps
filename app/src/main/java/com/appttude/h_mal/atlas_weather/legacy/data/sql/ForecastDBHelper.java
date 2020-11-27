package com.appttude.h_mal.atlas_weather.legacy.data.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry;

public class ForecastDBHelper extends SQLiteOpenHelper {

    public static final String name = "forecasts.db";
    public static final int version = 1;

    public ForecastDBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PRODUCTS_TABLE =  "CREATE TABLE " + ForecastEntry.TABLE_NAME + " ("
                + ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ForecastEntry.COLUMN_FORECAST_NAME + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE_2);
    }

    private static final String SQL_CREATE_PRODUCTS_TABLE_2 =  "CREATE TABLE " + ForecastEntry.TABLE_NAME_WIDGET + " ("
            + ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ForecastEntry.COLUMN_FORECAST_NAME + " TEXT NOT NULL, "
            + ForecastEntry.COLUMN_WIDGET_FORECAST_ITEM + " TEXT)";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
