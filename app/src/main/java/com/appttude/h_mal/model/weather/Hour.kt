package com.appttude.h_mal.model.weather

import android.os.Parcelable
import com.appttude.h_mal.utils.generateIconUrlString
import kotlinx.parcelize.Parcelize
import com.appttude.h_mal.data.network.response.forecast.Hour as ForecastHour


@Parcelize
data class Hour(
    val dt: Int? = null,
    val temp: Double? = null,
    val icon: String? = null
) : Parcelable {

    constructor(hour: ForecastHour) : this(
        hour.dt,
        hour.temp,
        generateIconUrlString(hour.weather?.getOrNull(0)?.icon)
    )
}