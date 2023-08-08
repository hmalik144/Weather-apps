package com.appttude.h_mal.atlas_weather.data.location

import android.content.Context
import android.location.Location
import com.appttude.h_mal.atlas_weather.BuildConfig
import com.appttude.h_mal.atlas_weather.utils.createSuspend
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.data.common.Address
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder

abstract class LocationHelper(
    context: Context
) {

    private val key = BuildConfig.ParamTwo
    private val searchApi = OnlineSearchApi.create(context, key)

    suspend fun getAddressFromLatLong(
        lat: Double, long: Double
    ): Address? {
        return createSuspend {
            val revGeoQuery =
                ReverseGeocoderSearchQueryBuilder(lat, long).build()

            val resultSingle =
                searchApi.reverseGeocoding(revGeoQuery)

            resultSingle.blockingGet()?.addresses?.get(0)?.address
        }

    }

    protected fun Location.getLatLonPair(): Pair<Double, Double> = Pair(latitude, longitude)
}