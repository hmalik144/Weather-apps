package com.appttude.h_mal.atlas_weather.data.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import java.io.IOException
import java.util.*


class LocationProvider(
        val applicationContext: Context
) {


    private var locationManager =
            applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager?

    private val geoCoder: Geocoder by lazy { Geocoder(applicationContext, Locale.getDefault()) }

    @RequiresPermission(value = ACCESS_FINE_LOCATION)
    fun getLatLong(): Pair<Double, Double>{
        val location = locationManager
                ?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location ?: throw IOException("Unable to get location")

        val lat = location.latitude
        val long = location.longitude
        return Pair(lat, long)
    }

    fun getLocationName(lat: Double, long: Double): String{
        val result = geoCoder.getFromLocation(lat, long, 1)?.get(0)

        return result?.let { location ->
            location.locality?.let { return it }
            location.subAdminArea?.let { return it }
        } ?: "$lat, $long"
    }

    fun getLatLongFromLocationString(location: String): Pair<Double, Double>{
        val locations = geoCoder.getFromLocationName(location, 1)

        locations?.takeIf { it.isNotEmpty() }?.get(0)?.let {
            return Pair(it.latitude, it.longitude)
        }

        throw IOException("No location found")
    }

}