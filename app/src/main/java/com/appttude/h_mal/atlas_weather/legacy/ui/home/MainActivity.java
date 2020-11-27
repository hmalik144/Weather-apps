package com.appttude.h_mal.atlas_weather.legacy.ui.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastDBHelper;
import com.appttude.h_mal.atlas_weather.legacy.services.notifcation.NotificationReceiver;
import com.appttude.h_mal.atlas_weather.legacy.ui.InfoActivity;
import com.appttude.h_mal.atlas_weather.legacy.ui.UnitSettingsActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    public ForecastDBHelper forecastsDbhelper;
    private String TAG = getClass().getSimpleName();

    public static NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        iconsInTabs(tabLayout);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean first_time_run = SP.getBoolean("FIRST_TIME_RUN",true);

        Log.i(TAG, "onCreate: notification setup" + first_time_run);

        if (first_time_run) {
            setupNotificationBroadcaster(getApplicationContext());

//                    cal.add(Calendar.SECOND, 5);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
//        }
        }

        iconsInTabs(tabLayout);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        forecastsDbhelper = new ForecastDBHelper(getApplicationContext());

    }

    private void iconsInTabs (TabLayout tabLayout){

        int[] tabIcons = {R.mipmap.ic_home,R.mipmap.ic_world};

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent i = new Intent(this, UnitSettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_into:
                Intent infoActivity = new Intent(this, InfoActivity.class);
                startActivity(infoActivity);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static String changeToInt(Double d){
        return String.valueOf(Math.round(d));
    }

    public static int getImageResource(String s, Context context){

        s =s.replace("cdn.apixu.com/weather/64x64/","");
        s = s.replace(".png","");
        s =s.replace("/","_");
        s = s.substring(2);

        return context.getResources().getIdentifier(s,"drawable",context.getPackageName());
    }

    public static String getLocationName(Context context, Double latitude, Double longitude) {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        String result = "";
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                result = address.getLocality();
            }
        }

        return result;
    }

    public static void setupNotificationBroadcaster(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 8);
        cal.set(Calendar.SECOND, 5);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs_menu/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);


        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    FragmentHome tab1 = new FragmentHome();
                    return tab1;
                case 1:
                    FragmentTwo tab2 = new FragmentTwo();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Home";
//                case 1:
//                    return "World";
//            }
            return null;
        }


    }
}
