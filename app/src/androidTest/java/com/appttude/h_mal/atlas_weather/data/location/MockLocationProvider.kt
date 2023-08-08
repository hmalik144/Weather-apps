package com.appttude.h_mal.atlas_weather.data.location

import com.appttude.h_mal.atlas_weather.model.types.LocationType

class MockLocationProvider : LocationProvider {
    private val latLong = Pair(0.00, 0.00)

    override suspend fun getCurrentLatLong() = latLong
    override fun getLatLongFromLocationName(location: String) = latLong

    override suspend fun getLocationNameFromLatLong(
        lat: Double,
        long: Double,
        type: LocationType
    ): String {
        return "Mock Location"
    }

}