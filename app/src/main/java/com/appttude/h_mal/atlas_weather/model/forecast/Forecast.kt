package com.appttude.h_mal.atlas_weather.model.forecast

import android.os.Parcel
import android.os.Parcelable
import com.appttude.h_mal.atlas_weather.model.weather.DailyWeather
import com.appttude.h_mal.atlas_weather.utils.toDayName
import com.appttude.h_mal.atlas_weather.utils.toDayString
import com.appttude.h_mal.atlas_weather.utils.toTime


data class Forecast(
    val date: String?,
    val day: String?,
    val condition: String?,
    val weatherIcon: String?,
    val mainTemp: String?,
    val minorTemp: String?,
    val averageTemp: String?,
    val windText: String?,
    val precipitation: String?,
    val humidity: String?,
    val uvi: String?,
    val sunrise: String?,
    val sunset: String?,
    val cloud: String?
): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor(dailyWeather: DailyWeather) : this(
        dailyWeather.dt?.toDayString(),
        dailyWeather.dt?.toDayName(),
        dailyWeather.description,
        dailyWeather.icon,
        dailyWeather.max?.toInt().toString(),
        dailyWeather.min?.toInt().toString(),
        dailyWeather.average?.toInt().toString(),
        dailyWeather.windSpeed?.toInt().toString(),
        (dailyWeather.pop?.times(100))?.toInt().toString(),
        dailyWeather.humidity?.toString(),
        dailyWeather.uvi?.toInt().toString(),
        dailyWeather.sunrise?.toTime(),
        dailyWeather.sunset?.toTime(),
        dailyWeather.clouds?.toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(day)
        parcel.writeString(condition)
        parcel.writeString(weatherIcon)
        parcel.writeString(mainTemp)
        parcel.writeString(minorTemp)
        parcel.writeString(averageTemp)
        parcel.writeString(windText)
        parcel.writeString(precipitation)
        parcel.writeString(humidity)
        parcel.writeString(uvi)
        parcel.writeString(sunrise)
        parcel.writeString(sunset)
        parcel.writeString(cloud)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Forecast> {
        override fun createFromParcel(parcel: Parcel): Forecast {
            return Forecast(parcel)
        }

        override fun newArray(size: Int): Array<Forecast?> {
            return arrayOfNulls(size)
        }
    }
}