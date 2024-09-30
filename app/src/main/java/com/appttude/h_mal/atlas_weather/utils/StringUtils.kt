package com.appttude.h_mal.atlas_weather.utils

import com.appttude.h_mal.atlas_weather.model.types.UnitType


fun generateIconUrlString(icon: String?): String? {
    return icon?.let {
        StringBuilder()
            .append("http://openweathermap.org/img/wn/")
            .append(it)
            .append("@2x.png")
            .toString()
    }
}

fun UnitType.getSymbol(): String = if (this == UnitType.METRIC) "°C" else "°F"