package com.appttude.h_mal.atlas_weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.appttude.h_mal.atlas_weather.MainActivity.changeToInt;
import static com.appttude.h_mal.atlas_weather.MainActivity.getImageResource;

public class CurrentForecastAdapter extends ArrayAdapter<ForecastItem> {
    Context context;
    private SharedPreferences mSettings;

    public CurrentForecastAdapter(@NonNull Context context, @NonNull List<ForecastItem> objects) {
        super(context, 0, objects);
        this.context = context;

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.db_list_item, parent,false);
        }

        CurrentForecast currentForecast = null;
        try {
            currentForecast = getItem(position).getCurrentForecast();
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "error: ", e);
        }finally {
            if (currentForecast != null){
                TextView location = listItemView.findViewById(R.id.db_location);
                location.setText(currentForecast.getLocation());

                TextView conditionTV = listItemView.findViewById(R.id.db_condition);
                conditionTV.setText(currentForecast.getCondition_text());

                ImageView weatherIV = listItemView.findViewById(R.id.db_icon);
                weatherIV.setImageResource(getImageResource(currentForecast.getIconURL(), context));

                TextView mainTempTV = listItemView.findViewById(R.id.db_main_temp);
                TextView tempUnit = listItemView.findViewById(R.id.db_minor_temp);
                if (mSettings.getString("temp_units","").equals("°F")){
                    mainTempTV.setText(changeToInt(currentForecast.getTemp_f()));
                    tempUnit.setText("°F");
                }else {
                    mainTempTV.setText(changeToInt(currentForecast.getTemp_c()));
                    tempUnit.setText("°C");
                }
            }
        }

        return listItemView;
    }

    private void openWorldItem(ForecastItem forcast){
        Intent i = new Intent(context,WorldItemActivity.class);
        i.putExtra("ForecastItem",forcast);
        context.startActivity(i);
    }
}
