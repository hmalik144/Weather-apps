package com.appttude.h_mal.atlas_weather.model.types

import java.util.Locale

enum class UnitType {
    METRIC,
    IMPERIAL;

    companion object {
        fun getByName(name: String?): UnitType? {
            return entries.firstOrNull {
                it.name.lowercase(Locale.ROOT) == name?.lowercase(
                    Locale.ROOT
                )
            }
        }

        fun UnitType.getLabel() = name.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}