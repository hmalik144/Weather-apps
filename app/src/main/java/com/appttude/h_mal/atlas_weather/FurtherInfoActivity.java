package com.appttude.h_mal.atlas_weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.appttude.h_mal.atlas_weather.MainActivity.changeToInt;

public class FurtherInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_further_info);

        Intent mIntent  = getIntent();
        Forecast forecast  = mIntent.getParcelableExtra("currentForcast");

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        TextView maxTemp = findViewById(R.id.maxtemp);
        TextView averageTemp = findViewById(R.id.averagetemp);
        TextView minimumTemp = findViewById(R.id.minimumtemp);
        TextView windText = findViewById(R.id.windtext);
        TextView precipText = findViewById(R.id.preciptext);
        TextView humidityText = findViewById(R.id.humiditytext);
        TextView uvText = findViewById(R.id.uvtext);
        TextView sunriseText = findViewById(R.id.sunrisetext);
        TextView sunsetText = findViewById(R.id.sunsettext);

        if (mSettings.getString("temp_units","").equals("°F")){
            maxTemp.setText(changeToInt(forecast.getMaxtemp_f()) + mSettings.getString("temp_units","°F"));
            averageTemp.setText(changeToInt(forecast.getAvgtemp_f())+mSettings.getString("temp_units","°F"));
            minimumTemp.setText(changeToInt(forecast.getMintemp_f())+mSettings.getString("temp_units","°F"));

        }else {
            maxTemp.setText(changeToInt(forecast.getMaxtemp_c())+mSettings.getString("temp_units","°C"));
            averageTemp.setText(changeToInt(forecast.getAvgtemp_c())+mSettings.getString("temp_units","°C"));
            minimumTemp.setText(changeToInt(forecast.getMintemp_c())+mSettings.getString("temp_units","°C"));
        }

        if (mSettings.getString("wind_units","").equals("mph")){
            windText.setText(String.valueOf(forecast.getMaxwind_mph()+mSettings.getString("wind_units","mhp")));
        }else {
            windText.setText(String.valueOf(forecast.getMaxwind_kph()+mSettings.getString("wind_units","kph")));
        }

        if (mSettings.getString("precip_units","").equals("in")){
            precipText.setText(String.valueOf(forecast.getTotalprecip_in()+mSettings.getString("precip_units","inches")));
        }else {
            precipText.setText(String.valueOf(forecast.getTotalprecip_mm()+ mSettings.getString("precip_units","mm")));
        }

        humidityText.setText(String.valueOf(forecast.getAvghumidity()));
        uvText.setText(String.valueOf(forecast.getUv()));
        sunriseText.setText(forecast.getSunrise());
        sunsetText.setText(forecast.getSunset());


    }
}
