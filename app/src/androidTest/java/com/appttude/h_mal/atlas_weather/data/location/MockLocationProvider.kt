package com.appttude.h_mal.atlas_weather.data.location

import com.appttude.h_mal.atlas_weather.model.types.LocationType

class MockLocationProvider : LocationProvider {
    private var feedMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf()

    private var latLong = Pair(0.00, 0.00)

    override suspend fun getCurrentLatLong() = latLong
    override fun getLatLongFromLocationName(location: String): Pair<Double, Double> {
        return feedMap[location] ?: Pair(0.00, 0.00)
    }

    override suspend fun getLocationNameFromLatLong(
        lat: Double,
        long: Double,
        type: LocationType
    ): String {
        return feedMap.filterValues { it.first == lat && it.second == long }.keys.firstOrNull() ?: "Mock Location"
    }

    fun addLocationToList(name: String, lat: Double, long: Double) {
        val latLong = Pair(lat, long)
        feedMap[name] = latLong
    }

    fun removeLocationFromList(name: String) {
        feedMap.remove(name)
    }

    fun removeLocationFromList(lat: Double, long: Double) {
        feedMap.filterValues { it.first == lat && it.second == long }.keys.firstOrNull()?.let {
            feedMap.remove(it)
        }
    }
}