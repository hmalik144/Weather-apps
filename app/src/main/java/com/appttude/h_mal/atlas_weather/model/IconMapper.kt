package com.appttude.h_mal.atlas_weather.model

enum class IconMapper(val label: String) {
    snow("13d"),
    snow_showers_day("13d"),
    snow_showers_night("13n"),
    thunder_rain("11d"),
    thunder_showers_day("11d"),
    thunder_showers_night("11n"),
    rain("10d"),
    showers_day("10d"),
    showers_night("10n"),
    fog("50d"),
    wind("50d"),
    cloudy("04d"),
    partly_cloudy_day("03d"),
    partly_cloudy_night("03n"),
    clear_day("01d"),
    clear_night("01n");

    companion object{
        fun findIconCode(iconId: String?): String? {
            val label = iconId?.replace("-", "_")
            val enumName = IconMapper.entries.find { it.name == label }
            return enumName?.label
        }
    }
}