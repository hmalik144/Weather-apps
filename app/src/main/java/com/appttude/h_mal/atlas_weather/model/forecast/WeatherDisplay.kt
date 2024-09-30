package com.appttude.h_mal.atlas_weather.model.forecast

import android.os.Parcel
import android.os.Parcelable
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.Hour


data class WeatherDisplay(
    val averageTemp: Double?,
    var unit: String?,
    var location: String?,
    var iconURL: String?,
    val description: String?,
    val hourly: List<Hour>?,
    val forecast: List<Forecast>?,
    val windSpeed: String?,
    val windDirection: String?,
    val precipitation: String?,
    val humidity: String?,
    val clouds: String?,
    val lat: Double = 0.00,
    val lon: Double = 0.00,
    var displayName: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Hour),
        parcel.createTypedArrayList(Forecast),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()
    )

    constructor(entity: EntityItem) : this(
        entity.weather.current?.temp,
        entity.weather.temperatureUnit,
        entity.id,
        entity.weather.current?.icon,
        entity.weather.current?.description,
        entity.weather.hourly,
        entity.weather.daily?.drop(1)?.map { Forecast(it) },
        entity.weather.current?.windSpeed?.toString(),
        entity.weather.current?.windDeg?.toString(),
        entity.weather.daily?.get(0)?.pop?.times(100)?.toInt()?.toString(),
        entity.weather.current?.humidity?.toString(),
        entity.weather.current?.clouds?.toString(),
        entity.weather.lat!!,
        entity.weather.lon!!,
        entity.weather.locationString
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(averageTemp)
        parcel.writeString(unit)
        parcel.writeString(location)
        parcel.writeString(iconURL)
        parcel.writeString(description)
        parcel.writeTypedList(hourly)
        parcel.writeTypedList(forecast)
        parcel.writeString(windSpeed)
        parcel.writeString(windDirection)
        parcel.writeString(precipitation)
        parcel.writeString(humidity)
        parcel.writeString(clouds)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(displayName)
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

