package com.appttude.h_mal.atlas_weather.model.forecast

import android.os.Parcel
import android.os.Parcelable
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather


data class WeatherDisplay(
        val averageTemp: Double?,
        var unit: String?,
        var location: String?,
        val iconURL: String?,
        val description: String?,
        val forecast: List<Forecast>?,
        val windSpeed: String?,
        val windDirection: String?,
        val precipitation: String?,
        val humidity: String?,
        val clouds: String?,
        val lat: Double = 0.00,
        val lon: Double = 0.00
): Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Forecast),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    constructor(weather: FullWeather) : this(
            weather.current?.temp,
            null,
            null,
            weather.current?.icon,
            weather.current?.description,
            weather.daily?.map { Forecast(it) },
            weather.current?.windSpeed?.toString(),
            weather.current?.windDeg?.toString(),
            weather.daily?.get(0)?.pop?.toString(),
            weather.current?.humidity?.toString(),
            weather.current?.clouds?.toString(),
            weather.lat,
            weather.lon
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(averageTemp)
        parcel.writeString(unit)
        parcel.writeString(location)
        parcel.writeString(iconURL)
        parcel.writeString(description)
        parcel.writeTypedList(forecast)
        parcel.writeString(windSpeed)
        parcel.writeString(windDirection)
        parcel.writeString(precipitation)
        parcel.writeString(humidity)
        parcel.writeString(clouds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherDisplay> {
        override fun createFromParcel(parcel: Parcel): WeatherDisplay {
            return WeatherDisplay(parcel)
        }

        override fun newArray(size: Int): Array<WeatherDisplay?> {
            return arrayOfNulls(size)
        }
    }
}

