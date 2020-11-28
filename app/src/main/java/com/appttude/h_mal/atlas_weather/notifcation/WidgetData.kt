package com.appttude.h_mal.atlas_weather.notifcation

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class WidgetData(
        val location: String?,
        val icon: Bitmap?,
        val currentTemp: String?,
        val list: List<InnerWidgetData>? = listOf()
):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Bitmap::class.java.classLoader),
            parcel.readString(),
            parcel.createTypedArrayList(InnerWidgetData)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(location)
        parcel.writeParcelable(icon, flags)
        parcel.writeString(currentTemp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WidgetData> {
        override fun createFromParcel(parcel: Parcel): WidgetData {
            return WidgetData(parcel)
        }

        override fun newArray(size: Int): Array<WidgetData?> {
            return arrayOfNulls(size)
        }
    }

}

data class InnerWidgetData(
        val date: String?,
        val icon: Bitmap?,
        val currentTemp: String?
):Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Bitmap::class.java.classLoader),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeParcelable(icon, flags)
        parcel.writeString(currentTemp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InnerWidgetData> {
        override fun createFromParcel(parcel: Parcel): InnerWidgetData {
            return InnerWidgetData(parcel)
        }

        override fun newArray(size: Int): Array<InnerWidgetData?> {
            return arrayOfNulls(size)
        }
    }

}

interface WidgetDataImplementation{
    fun getWidgetData(): WidgetData
}