package com.appttude.h_mal.atlas_weather;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;

import com.appttude.h_mal.atlas_weather.AppWidget.NewAppWidget;

import static com.appttude.h_mal.atlas_weather.MainActivity.setupNotificationBroadcaster;

public class UnitSettings extends PreferenceActivity {

    private String TAG = getClass().getSimpleName();
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        //listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                if(key.equals("temp_units")){
                    Intent intent = new Intent(getBaseContext(), NewAppWidget.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                    int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(intent);
                }

                if(key.equals("notif_boolean")){
                    setupNotificationBroadcaster(getBaseContext());
                }


            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        Log.i(TAG, "onSharedPreferenceChanged: " + s);
//        if (s == "temp_units"){
//            Intent intent = new Intent(getBaseContext(), NewAppWidget.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//
//            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//            sendBroadcast(intent);
//        }
//    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

        }
    }
}
