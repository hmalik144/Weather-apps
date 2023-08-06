package com.appttude.h_mal.data.location

import com.appttude.h_mal.model.types.LocationType

interface LocationProvider {
    suspend fun getCurrentLatLong(): Pair<Double, Double>
    fun getLatLongFromLocationName(location: String): Pair<Double, Double>
    suspend fun getLocationNameFromLatLong(
            lat: Double,
            long: Double,
            type: LocationType = LocationType.Town
    ): String
}