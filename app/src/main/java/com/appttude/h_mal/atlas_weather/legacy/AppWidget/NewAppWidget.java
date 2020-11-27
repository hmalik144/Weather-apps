package com.appttude.h_mal.atlas_weather.legacy.AppWidget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastDBHelper;
import com.appttude.h_mal.atlas_weather.legacy.model.CurrentForecast;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;
import com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong;
import com.appttude.h_mal.atlas_weather.legacy.ui.FurtherInfoActivity;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.UriBuilder;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.createUrl;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.extractFeatureFromJson;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.makeHttpRequest;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.COLUMN_FORECAST_NAME;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.COLUMN_WIDGET_FORECAST_ITEM;
import static com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry.TABLE_NAME_WIDGET;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.changeToInt;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getImageResource;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getLocationName;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider{

    private static String TAG = NewAppWidget.class.getSimpleName();
    private static int request = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            getLatLong.configLatLong(context);
            forecastAsyncTask task = new forecastAsyncTask(context,appWidgetManager,appWidgetId);
            task.execute(UriBuilder(6));
            Log.i(TAG, "onUpdate: widget onUpdate called at " + getCurrentTimeUsingDate());

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {


        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.widget_listview);

            Log.i(TAG, "onEnabled: called at " + getCurrentTimeUsingDate());
        }catch (Exception e){
            Log.e(TAG, "onEnabled: ", e);
        }
        // Enter relevant functionality for when the first widget is created
//        getLatLong.configLatLong(context);
//        forecastAsyncTask task = new forecastAsyncTask();
//        task.execute(UriBuilder(5));
    }



    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.widget_listview);
            Log.i(TAG, "onReceive: widget onReceive called at " + getCurrentTimeUsingDate());

        }

        super.onReceive(context, intent);
    }

    static class forecastAsyncTask extends AsyncTask<String,Void,String>{

        private Context context;
        AppWidgetManager appWidgetManager;
        int appWidgetId;

        public forecastAsyncTask(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
            this.context = context;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetId = appWidgetId;
        }

        @Override
        protected String doInBackground(String... urlString) {
            Log.i(TAG, "doInBackground: started at " + getCurrentTimeUsingDate());
            String jsonResponse = null;

            if (urlString.length < 1 || urlString[0] == null) {
                return null;
            }
            try {
                URL url = createUrl(urlString[0]);
                jsonResponse = makeHttpRequest(url);

            } catch (IOException e) {
                Log.e( TAG, "Problem making the HTTP request.", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "onPostExecute: SQL data result: " + result);
            
            if(result == null){
                Log.i(TAG, "onPostExecute: result null");
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            Intent intentUpdate = new Intent(context, NewAppWidget.class);
            intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int[] idArray = new int[]{appWidgetId};
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

            PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                    context, request++, intentUpdate,
                    PendingIntent.FLAG_UPDATE_CURRENT);

//            float opacity = 0.3f;           //opacity = 0: fully transparent, opacity = 1: no transparancy
//            int backgroundColor = 0x000000; //background color (here black)
//            views.setInt( R.id.whole_widget_view, "setBackgroundColor", (int)(opacity * 0xFF) << 24 | backgroundColor);

            if (result != null) {

                ForecastDBHelper forecastsDbhelper = new ForecastDBHelper(context);
                SQLiteDatabase database = forecastsDbhelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_FORECAST_NAME, "Current");
                values.put(COLUMN_WIDGET_FORECAST_ITEM, result);

                String[] projection = {COLUMN_FORECAST_NAME, COLUMN_WIDGET_FORECAST_ITEM};

                Cursor cursor = null;
                try {
                    cursor = database.query(TABLE_NAME_WIDGET,
                            projection,
                            COLUMN_FORECAST_NAME + " IS ?",
                            new String[]{"Current"},
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, "onPostExecute: ", e);
                } finally {
                    if(cursor != null){
                        while (cursor.moveToNext()){
                            String currentDB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WIDGET_FORECAST_ITEM));
                            Log.i(TAG, "onPostExecute: current db: " + currentDB);
                        }
                    }

                    if (cursor != null && cursor.getCount() > 0) {
                        database.update(TABLE_NAME_WIDGET, values, COLUMN_FORECAST_NAME + " IS ?", new String[]{"Current"});
                        Log.i(TAG, "onPostExecute: attempted to update sql, size:" + cursor.getCount());
                        cursor.close();
                    } else {
                        database.insert(TABLE_NAME_WIDGET, null, values);
                        Log.i(TAG, "onPostExecute: attempted to insert sql");
                    }
                }

                ForecastItem forecastItem = extractFeatureFromJson(result);
                CurrentForecast cf = forecastItem.getCurrentForecast();

                Intent intent = new Intent(context, WidgetRemoteViewsService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
                String temp;
                String unit;
                if (mSettings.getString("temp_units","").equals("°F")){
                    temp =changeToInt(cf.getTemp_f());
                    unit = "°F";
                }else {
                    temp = changeToInt(cf.getTemp_c());
                    unit = "°C";
                }

                views.setRemoteAdapter(R.id.widget_listview, intent);
                views.setTextViewText(R.id.widget_main_temp,temp);
                views.setTextViewText(R.id.widget_feel_temp,unit);
                views.setTextViewText(R.id.dash," / ");
                views.setTextViewText(R.id.widget_item_temp_high,changeToInt(forecastItem.getForecastArrayList().get(0).getMaxtemp_c())+"°");
                views.setTextViewText(R.id.widget_item_temp_low,changeToInt(forecastItem.getForecastArrayList().get(0).getMintemp_c())+"°");
                views.setTextViewText(R.id.widget_current_location,getLocationName(context,cf.getLatitude(),cf.getLongitude()));
                views.setImageViewResource(R.id.location_icon, R.drawable.location_flag);
                views.setImageViewResource(R.id.widget_current_icon,
                        getImageResource(cf.getIconURL(),context));

                Intent clickIntentTemplate = new Intent(context, FurtherInfoActivity.class);
                PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(clickIntentTemplate)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate);

                views.setOnClickPendingIntent(R.id.widget_current_icon, pendingUpdate);
                views.setOnClickPendingIntent(R.id.widget_current_location,pendingUpdate);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
                Log.i(TAG, "onPostExecute: finished at " + getCurrentTimeUsingDate());
            }
            else {
                Log.i(TAG, "onPostExecute: null part executed");

                views.setTextViewText(R.id.widget_current_location,"Refresh");
                views.setImageViewResource(R.id.widget_current_icon, R.drawable.widget_error_icon);
                views.setImageViewResource(R.id.location_icon, R.drawable.refreshing);

                views.setOnClickPendingIntent(R.id.widget_current_icon, pendingUpdate);
                views.setOnClickPendingIntent(R.id.widget_current_location,pendingUpdate);

                appWidgetManager.updateAppWidget(appWidgetId, views);
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
                //add a listener to the view

            }
        }
    }


    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss.SSS";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        return dateFormat.format(date);
    }
}

