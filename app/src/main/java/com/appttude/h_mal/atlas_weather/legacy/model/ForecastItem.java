package com.appttude.h_mal.atlas_weather.legacy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ForecastItem implements Parcelable {

    private CurrentForecast currentForecast;
    private List<Forecast> forecastArrayList;

    public ForecastItem(CurrentForecast currentForecast, List<Forecast> forecastArrayList) {
        this.currentForecast = currentForecast;
        this.forecastArrayList = forecastArrayList;
    }

    protected ForecastItem(Parcel in) {
        currentForecast = in.readParcelable(CurrentForecast.class.getClassLoader());
        forecastArrayList = in.createTypedArrayList(Forecast.CREATOR);
    }

    public static final Creator<ForecastItem> CREATOR = new Creator<ForecastItem>() {
        @Override
        public ForecastItem createFromParcel(Parcel in) {
            return new ForecastItem(in);
        }

        @Override
        public ForecastItem[] newArray(int size) {
            return new ForecastItem[size];
        }
    };

    public CurrentForecast getCurrentForecast() {
        return currentForecast;
    }

    public List<Forecast> getForecastArrayList() {
        return forecastArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(currentForecast, i);
        parcel.writeTypedList(forecastArrayList);
    }
}
