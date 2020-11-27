package com.appttude.h_mal.atlas_weather.legacy.services.notifcation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON;
import com.appttude.h_mal.atlas_weather.legacy.model.CurrentForecast;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;
import com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong;
import com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity;

import java.io.IOException;
import java.net.URL;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.createUrl;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.extractFeatureFromJson;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.makeHttpRequest;
import static com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong.latitude;
import static com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong.longitude;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.changeToInt;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getImageResource;

/**
 * Created by h_mal on 29/04/2018.
 */

public class NotificationReceiver extends BroadcastReceiver{

    private String TAG = getClass().getSimpleName();

    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel_1";
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        Log.i(TAG, "onReceive: notif fired");

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);

        boolean notif = SP.getBoolean("notif_boolean",true);

        if(notif) {
            try {
                getLatLong.configLatLong(mContext);
            } catch (Exception e) {
                Log.e(TAG, "onReceive: ", e);
            } finally {
                if (latitude != null && longitude != null) {
                    String stringURL = RetrieveJSON.UriBuilder(5);
                    NotifAsyncTask task = new NotifAsyncTask();
                    task.execute(stringURL);
                }
            }
        }

        SP.edit().putBoolean("FIRST_TIME_RUN",false).apply();

    }

    private void pushNotif(Context context, ForecastItem forecastItem){
        Intent notificationIntent = new Intent(context, MainActivity.class);

        CurrentForecast currentForecast = forecastItem.getCurrentForecast();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        String temp;
        if (mSettings.getString("temp_units","").equals("°F")){
            temp =changeToInt(currentForecast.getTemp_f());
        }else {
            temp = changeToInt(currentForecast.getTemp_c());

        }

        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setContentTitle("Weather App")
                .setContentText(temp + "° - " + currentForecast.getCondition_text())
                .setSmallIcon(R.mipmap.ic_notif) //change icon
                .setLargeIcon(Icon.createWithResource(context,getImageResource(forecastItem.getCurrentForecast().getIconURL(),context)))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }

    private class NotifAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urlString) {
            String jsonResponse = null;

            if (urlString.length < 1 || urlString[0] == null) {
                return null;
            }
            try {
                URL url = createUrl(urlString[0]);
                jsonResponse = makeHttpRequest(url);

            } catch (IOException e) {
                Log.e(TAG, "Problem making the HTTP request.", e);
            }

            return jsonResponse;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "onPostExecute: " +result);
            if (result != null && !result.isEmpty()) {
                final ForecastItem forecastItem = extractFeatureFromJson(result);


                pushNotif(mContext,forecastItem);

            }
        }

    }
}
