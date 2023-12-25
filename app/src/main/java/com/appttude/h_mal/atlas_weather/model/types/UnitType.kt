package com.appttude.h_mal.atlas_weather.model.types

import java.util.Locale

enum class UnitType {
    METRIC,
    IMPERIAL;

    companion object {
        fun getByName(name: String?): UnitType? {
            return values().firstOrNull {
                it.name.lowercase(Locale.ROOT) == name?.lowercase(
                    Locale.ROOT
                )
            }
        }
    }
}