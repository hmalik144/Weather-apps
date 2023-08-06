package com.appttude.h_mal.data.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.HandlerThread
import androidx.annotation.RequiresPermission
import com.appttude.h_mal.model.types.LocationType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LocationProviderImpl(
    private val applicationContext: Context
) : LocationProvider, LocationHelper(applicationContext) {
    private var locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    private val client = FusedLocationProviderClient(applicationContext)
    private val geoCoder: Geocoder by lazy { Geocoder(applicationContext, Locale.getDefault()) }

    @RequiresPermission(value = ACCESS_COARSE_LOCATION)
    override suspend fun getCurrentLatLong(): Pair<Double, Double> {
        val location = client.lastLocation.await() ?: getAFreshLocation()
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
        return client.getCurrentLocation(PRIORITY_LOW_POWER, object : CancellationToken() {
            override fun isCancellationRequested(): Boolean = false
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken = this
        }).await()
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestFreshLocation(): Location? {
        val handlerThread = HandlerThread("MyHandlerThread")
        handlerThread.start()
        // Now get the Looper from the HandlerThread
        // NOTE: This call will block until the HandlerThread gets control and initializes its Looper
        val looper = handlerThread.looper

        return suspendCoroutine { cont ->
            val callback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    client.removeLocationUpdates(this)
                    cont.resume(p0?.lastLocation)
                }
            }

            with(locationManager!!) {
                when {
                    isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                        client.requestLocationUpdates(createLocationRequest(PRIORITY_HIGH_ACCURACY), callback, looper)
                    }
                    isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                        client.requestLocationUpdates(createLocationRequest(PRIORITY_LOW_POWER), callback, looper)
                    }
                    else -> {
                        cont.resume(null)
                    }
                }
            }

        }
    }

    private fun createLocationRequest(priority: Int) = LocationRequest.create()
            .setPriority(priority)
            .setNumUpdates(1)
            .setExpirationDuration(1000)
}