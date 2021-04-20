package com.appttude.h_mal.atlas_weather.utils


fun generateIconUrlString(icon: String?): String?{
    return icon?.let {
        StringBuilder()
                .append("https://openweathermap.org/img/wn/")
                .append(it)
                .append("@4x.png")
                .toString()
    }
}