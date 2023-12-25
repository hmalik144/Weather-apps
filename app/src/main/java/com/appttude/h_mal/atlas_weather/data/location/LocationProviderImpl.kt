package com.appttude.h_mal.atlas_weather.data.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.*


class LocationProviderImpl(
    private val applicationContext: Context
) : LocationProvider, LocationHelper(applicationContext) {
    private val client = LocationServices.getFusedLocationProviderClient(applicationContext)
    private val geoCoder: Geocoder by lazy { Geocoder(applicationContext, Locale.getDefault()) }

    @RequiresPermission(value = ACCESS_COARSE_LOCATION)
    override suspend fun getCurrentLatLong(): Pair<Double, Double> {
        val lastLocation = client.lastLocation.await()
        lastLocation?.let {
            val delta = it.time - System.currentTimeMillis()
            if (delta < 300000) return it.getLatLonPair()
        }

        val location = getAFreshLocation()
        return location?.getLatLonPair() ?: throw IOException("Unable to get location")
    }

    override fun getLatLongFromLocationName(location: String): Pair<Double, Double> {
        val locations = geoCoder.getFromLocationName(location, 1)

        locations?.takeIf { it.isNotEmpty() }?.get(0)?.let {
            return Pair(it.latitude, it.longitude)
        }
        throw IOException("No location found")
    }

    override suspend fun getLocationNameFromLatLong(
        lat: Double, long: Double, type: LocationType
    ): String {
        val address = getAddressFromLatLong(lat, long) ?: return "$lat $long"

        return when (type) {
            LocationType.Town -> {
                val location = address
                    .municipalitySubdivision
                    ?.takeIf { it.isNotBlank() }
                    ?: address.municipality
                location ?: throw IOException("No location municipalitySubdivision or municipality")
            }

            LocationType.City -> {
                address.municipality ?: throw IOException("No location municipality")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getAFreshLocation(): Location? {
        return client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken
        ).await()
    }

    private val cancellationToken = object : CancellationToken() {
        override fun isCancellationRequested(): Boolean = false
        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
            this
    }

}