package com.appttude.h_mal.atlas_weather.model.weather

import android.os.Parcel
import android.os.Parcelable
import com.appttude.h_mal.atlas_weather.utils.generateIconUrlString
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.Hour as ForecastHour


data class Hour(
    val dt: Int? = null,
    val temp: Double? = null,
    val icon: String? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    constructor(hour: ForecastHour) : this(
        hour.dt,
        hour.temp,
        generateIconUrlString(hour.weather?.getOrNull(0)?.icon)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dt)
        parcel.writeValue(temp)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hour> {
        override fun createFromParcel(parcel: Parcel): Hour {
            return Hour(parcel)
        }

        override fun newArray(size: Int): Array<Hour?> {
            return arrayOfNulls(size)
        }
    }
}