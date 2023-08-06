package com.appttude.h_mal.atlas_weather.utils


fun generateIconUrlString(icon: String?): String?{
    return icon?.let {
        StringBuilder()
                .append("http://openweathermap.org/img/wn/")
                .append(it)
                .append("@2x.png")
                .toString()
    }
}