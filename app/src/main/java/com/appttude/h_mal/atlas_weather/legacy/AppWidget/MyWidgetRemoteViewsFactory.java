package com.appttude.h_mal.atlas_weather.legacy.AppWidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastDBHelper;
import com.appttude.h_mal.atlas_weather.legacy.model.Forecast;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.extractFeatureFromJson;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.COLUMN_FORECAST_NAME;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.COLUMN_WIDGET_FORECAST_ITEM;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.TABLE_NAME_WIDGET;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getImageResource;

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
//    private ForecastItem forecastItem;
    private List<Forecast> forecastList;
    private String TAG = this.getClass().getSimpleName();
    private Cursor cursor;
    private SQLiteDatabase database;
    private int appWidgetId;
    private SharedPreferences mSettings;

    public MyWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: widget oncreate executed");
        ForecastDBHelper forecastsDbhelper = new ForecastDBHelper(mContext);
        database = forecastsDbhelper.getWritableDatabase();

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null){
            cursor.close();
        }
        String[] projection = {COLUMN_FORECAST_NAME, COLUMN_WIDGET_FORECAST_ITEM};

        cursor = database.query(TABLE_NAME_WIDGET,
                projection,
                COLUMN_FORECAST_NAME + " IS ?",new String[]{"Current"},null,null,null);

        String json;
        if (cursor != null && cursor.getCount() > 0) {
            Log.i(TAG, "onDataSetChanged: cursor loaded, count: " + cursor.getCount());
            cursor.moveToFirst();
            json = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WIDGET_FORECAST_ITEM));
            ForecastItem forecastItem = extractFeatureFromJson(json);
            if (forecastItem != null) {
                forecastList = forecastItem.getForecastArrayList();
                forecastList.remove(0);
            }
            cursor.close();
        }else{
            Log.i(TAG, "onDataSetChanged: cursor is null");
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount: size = " +  forecastList.size());
        return forecastList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION ||
                forecastList == null || forecastList.get(i) == null ) {
            Log.i(this.getClass().getSimpleName(), "getViewAt: no views" );
            return null;
        }

        Log.i(this.getClass().getSimpleName(), "getViewAt: views exist " + i);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Date updatedate = new Date(forecastList.get(i).getDate_epoch()*1000);
        SimpleDateFormat format = new SimpleDateFormat("EEE");

        String dateText = format.format(updatedate);
//        if (dateText.equals("Wednesday")){
//            dateText = "Wednes..";
//        }
//
//        Log.i(TAG, "getViewAt: dateText: " + dateText);
//
//        if(i == 0){
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                String result = sdf.format(Calendar.getInstance().getTime());
//                rv.setTextViewText(R.id.widget_item_day, result);
//        }else {
            rv.setTextViewText(R.id.widget_item_day, dateText);
//        }

        rv.setImageViewResource(R.id.widget_item_image,
                getImageResource(forecastList.get(i).getIconURL(),mContext));

        String maxtemp;
        String mintemp;

        if (mSettings.getString("temp_units","").equals("F°")){
            maxtemp = String.valueOf(Math.round(forecastList.get(i).getMaxtemp_f())) + "°";
            mintemp = String.valueOf(Math.round(forecastList.get(i).getMintemp_f())) + "°";
        }else{
            maxtemp = String.valueOf(Math.round(forecastList.get(i).getMaxtemp_c())) + "°";
            mintemp = String.valueOf(Math.round(forecastList.get(i).getMintemp_c())) + "°";
        }

        rv.setTextViewText(R.id.widget_item_temp_high,maxtemp);
        rv.setTextViewText(R.id.widget_item_temp_low,mintemp);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("currentForcast", forecastList.get(i));
        rv.setOnClickFillInIntent(R.id.widget_item_layout, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
