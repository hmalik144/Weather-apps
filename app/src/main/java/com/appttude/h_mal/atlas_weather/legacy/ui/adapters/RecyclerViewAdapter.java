package com.appttude.h_mal.atlas_weather.legacy.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.model.CurrentForecast;
import com.appttude.h_mal.atlas_weather.legacy.model.Forecast;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;
import com.appttude.h_mal.atlas_weather.legacy.ui.FurtherInfoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.changeToInt;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getImageResource;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.getLocationName;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ForecastItem forecastItem;

    private CurrentForecast currentForecast;
    private List<Forecast> forecast;
    private SharedPreferences mSettings;
    private String location;



    class ViewHolderCurrent extends RecyclerView.ViewHolder {
        TextView locationTV;
        TextView conditionTV;
        ImageView weatherIV;
        TextView avgTempTV;
        TextView feels;
        TextView tempUnit;

        public ViewHolderCurrent(View listItemView) {
            super(listItemView);
            locationTV = listItemView.findViewById(R.id.location_main_4);
            conditionTV = listItemView.findViewById(R.id.condition_main_4);
            weatherIV = listItemView.findViewById(R.id.icon_main_4);
            avgTempTV = listItemView.findViewById(R.id.temp_main_4);
//            feels = listItemView.findViewById(R.id.feelstemp);
            tempUnit = listItemView.findViewById(R.id.temp_unit_4);
        }
    }

    class ViewHolderForecast extends RecyclerView.ViewHolder {

        TextView dateTV;
        TextView dayTV;
        TextView conditionTV;
        ImageView weatherIV;
        TextView mainTempTV;
        TextView minorTempTV;

        public ViewHolderForecast(View itemView){
            super(itemView);
            dateTV = itemView.findViewById(R.id.list_date);
            dayTV = itemView.findViewById(R.id.list_day);
            conditionTV = itemView.findViewById(R.id.list_condition);
            weatherIV = itemView.findViewById(R.id.list_icon);
            mainTempTV = itemView.findViewById(R.id.list_main_temp);
            minorTempTV = itemView.findViewById(R.id.list_minor_temp);
        }
    }

    class ViewHolderFurtherDetails extends RecyclerView.ViewHolder {

        TextView windSpeed;
        TextView windDirection;
        TextView precipitation;
        TextView humidity;
        TextView clouds;


        public ViewHolderFurtherDetails(View itemView){
            super(itemView);
            windSpeed = itemView.findViewById(R.id.windspeed);
            windDirection = itemView.findViewById(R.id.winddirection);
            precipitation = itemView.findViewById(R.id.precip_);
            humidity = itemView.findViewById(R.id.humidity_);
            clouds = itemView.findViewById(R.id.clouds_);
        }
    }

    public RecyclerViewAdapter(@NonNull Context context, @NonNull ForecastItem forecastItem, Double latitude, Double longitude) {
        this.context = context;
        this.forecastItem = forecastItem;
        this.currentForecast = forecastItem.getCurrentForecast();
        this.location = getLocationName(context,currentForecast.getLatitude(),currentForecast.getLongitude());


        List<Forecast> f = forecastItem.getForecastArrayList();
        f.remove(0);
        forecast = f;

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public RecyclerViewAdapter(@NonNull Context context, @NonNull ForecastItem forecastItem) {
        this.context = context;
        this.forecastItem = forecastItem;
        this.currentForecast = forecastItem.getCurrentForecast();

        List<Forecast> f = forecastItem.getForecastArrayList();
        f.remove(0);
        forecast = f;

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewCurrent = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item4, parent, false);
                return new ViewHolderCurrent(viewCurrent);

            case 2:
                View viewForecast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout2, parent, false);
                return new ViewHolderForecast(viewForecast);

            case 3:
                View viewFurther = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item3, parent, false);
                return new ViewHolderFurtherDetails(viewFurther);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 1:
                final ViewHolderCurrent viewHolderCurrent = (ViewHolderCurrent) holder;

                if (location != null) {
                    viewHolderCurrent.locationTV.setText(location);
                }else{
                    viewHolderCurrent.locationTV.setText(currentForecast.getLocation());
                }
                viewHolderCurrent.conditionTV.setText(currentForecast.getCondition_text());

                viewHolderCurrent.weatherIV.setImageResource(getImageResource(currentForecast.getIconURL(),context));

                if (mSettings.getString("temp_units","").equals("°F")){
                    viewHolderCurrent.avgTempTV.setText(changeToInt(currentForecast.getTemp_f()));
//                    viewHolderCurrent.feels.setText(changeToInt(currentForecast.getFeelslike_f()));
                    viewHolderCurrent.tempUnit.setText("°F");
                }else {
                    viewHolderCurrent.avgTempTV.setText(changeToInt(currentForecast.getTemp_c()));
//                    viewHolderCurrent.feels.setText(changeToInt(currentForecast.getFeelslike_c()));
                    viewHolderCurrent.tempUnit.setText("°C");
                }

                break;

            case 2:
                final ViewHolderForecast viewHolderForecast = (ViewHolderForecast) holder;
                final Forecast f = forecast.get(position -1);

                Date updatedate = new Date(f.getDate_epoch() * 1000);
                SimpleDateFormat format = new SimpleDateFormat("EEEE");
                String day = format.format(updatedate);
                viewHolderForecast.dayTV.setText(day);
                format = new SimpleDateFormat("d MMM");
                String date = format.format(updatedate);
                viewHolderForecast.dateTV.setText(date);

                if(f.getForecast_text().equals("Moderate or heavy rain shower")){
                    viewHolderForecast.conditionTV.setText("Moderate/Heavy Showers");
                }else{
                    viewHolderForecast.conditionTV.setText(f.getForecast_text());
                }

                viewHolderForecast.weatherIV.setImageResource(getImageResource(f.getIconURL(), context));

                if (mSettings.getString("temp_units","").equals("°F")){
                    viewHolderForecast.mainTempTV.setText(changeToInt(f.getMaxtemp_f()));
                    viewHolderForecast.minorTempTV.setText(changeToInt(f.getMintemp_f()));
                }else {
                    viewHolderForecast.mainTempTV.setText(changeToInt(f.getMaxtemp_c()));
                    viewHolderForecast.minorTempTV.setText(changeToInt(f.getMintemp_c()));
                }

                viewHolderForecast.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openFurtherInfo(f);
                    }
                });

                break;

            case 3:
                final ViewHolderFurtherDetails viewHolderFurtherDetails = (ViewHolderFurtherDetails) holder;


                if (mSettings.getString("wind_units","").equals("mph")) {
                    viewHolderFurtherDetails.windSpeed.setText(new StringBuilder().append(changeToInt(currentForecast.getWind_mph()))
                            .append(context.getResources().getStringArray(R.array.list_preference_wind_values)[0]));
                }else{
                    viewHolderFurtherDetails.windSpeed.setText(new StringBuilder().append(changeToInt(currentForecast.getWind_kph()))
                            .append(context.getResources().getStringArray(R.array.list_preference_wind_values)[1]).toString());

                }
                viewHolderFurtherDetails.windDirection.setText(currentForecast.getWind_dir());
                viewHolderFurtherDetails.humidity.setText(new StringBuilder().append(changeToInt(currentForecast.getHumidity()))
                        .append("%").toString());

                if (mSettings.getString("precip_units","").equals("in")) {
                    viewHolderFurtherDetails.precipitation.setText(new StringBuilder().append(changeToInt(currentForecast.getPrecip_mm()))
                            .append(context.getResources().getStringArray(R.array.list_preference_precip_values)[1]));
                }else{
                    viewHolderFurtherDetails.precipitation.setText(new StringBuilder().append(changeToInt(currentForecast.getPrecip_mm()))
                            .append(context.getResources().getStringArray(R.array.list_preference_precip_values)[0]));
                }
                viewHolderFurtherDetails.clouds.setText(changeToInt(currentForecast.getCloud()) + "%");
        }
    }

    @Override
    public int getItemCount() {
        return forecastItem.getForecastArrayList().size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position == 0){
            type = 1;
        }else if (position >= 1 && position < getItemCount() -1){
            type = 2;
        }else if (position == getItemCount() -1){
            type = 3;
        }
        return type;
    }

    private void openFurtherInfo(Forecast forcast){
        Intent i = new Intent(context, FurtherInfoActivity.class);
        i.putExtra("currentForcast",forcast);
        context.startActivity(i);
    }


}
