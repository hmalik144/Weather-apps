package com.appttude.h_mal.atlas_weather.data.location


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationProviderImplTest {
    private val lat = 51.558
    private val long = -0.091
    private val town = "Highbury"
    private val city = "London"

    lateinit var locationProvider: LocationProviderImpl

    @Before
    fun setUp() {
        val appContext =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        locationProvider = LocationProviderImpl(appContext)
    }

    @Test
    fun getLatLongFromLocationName_correctLatLongReturned() {
        // Act
        val retrievedLocation = locationProvider.getLatLongFromLocationName(town)
        val retrievedLat = retrievedLocation.first
        val retrievedLong = retrievedLocation.second

        // Assert
        assertRangeOfDouble(retrievedLat, lat, 0.1)
        assertRangeOfDouble(retrievedLong, long, 0.1)
    }

    @Test
    fun getLatLongFromLocationName_throwReturned() {
        // Arrange
        val randomString = "gHKJkhgkj"

        try {
            // Act
            locationProvider.getLatLongFromLocationName(randomString)
        } catch (e: IOException) {
            // Assert
            assertEquals(e.message, "No location found")
        }
    }


    @Test
    fun getLocationNameFromLatLong_locationTypeIsDefault_correctLocationReturned() = runBlocking {
        // Act
        val retrievedLocation = locationProvider.getLocationNameFromLatLong(lat, long)

        // Assert
        assertEquals(retrievedLocation, town)
    }

    @Test
    fun getLocationNameFromLatLong_locationTypeIsCity_correctLocationReturned() = runBlocking {
        // Act
        val retrievedLocation =
            locationProvider.getLocationNameFromLatLong(lat, long, LocationType.City)

        // Assert
        assertEquals(retrievedLocation, city)
    }

    private fun assertRangeOfDouble(input: Double, expected: Double, range: Double) {
        assertEquals(expected, input, range)
    }
}