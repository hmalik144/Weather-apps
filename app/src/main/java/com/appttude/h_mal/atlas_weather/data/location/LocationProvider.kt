package com.appttude.h_mal.atlas_weather.data.location

import android.Manifest
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.model.types.LocationType

interface LocationProvider {
    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    suspend fun getCurrentLatLong(): Pair<Double, Double>
    fun getLatLongFromLocationName(location: String): Pair<Double, Double>
    suspend fun getLocationNameFromLatLong(
        lat: Double,
        long: Double,
        type: LocationType = LocationType.Town
    ): String
}