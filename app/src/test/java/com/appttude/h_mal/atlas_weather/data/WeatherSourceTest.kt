package com.appttude.h_mal.atlas_weather.data

import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.weather.WeatherApiResponse
import com.appttude.h_mal.atlas_weather.data.repository.RepositoryImpl
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.properties.Delegates
import kotlin.test.assertEquals

class WeatherSourceTest : BaseTest() {

    @InjectMockKs
    lateinit var weatherSource: WeatherSource

    @MockK
    lateinit var repository: RepositoryImpl

    @MockK
    lateinit var locationProvider: LocationProviderImpl

    private lateinit var weatherResponse: WeatherApiResponse
    private var lat by Delegates.notNull<Double>()
    private var long by Delegates.notNull<Double>()
    private lateinit var latlon: Pair<Double, Double>
    private lateinit var fullWeather: FullWeather

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        weatherResponse = getTestData("new_response.json", WeatherApiResponse::class.java)
        lat = weatherResponse.latitude!!
        long = weatherResponse.longitude!!
        latlon = Pair(lat, long)
        fullWeather = weatherResponse.mapData().apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        }
    }

    @Test
    fun fetchDataForSingleLocation_validLocation_validReturn() {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)

        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            repository.getWeatherFromApi(
                lat.toString(),
                long.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        every { repository.getUnitType() } returns UnitType.METRIC
        every { repository.saveLastSavedAt(CURRENT_LOCATION) } returns Unit
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) } returns Unit

        // Assert
        val result =
            runBlocking { weatherSource.getWeather(latlon, locationType = LocationType.City) }
        assertEquals(result, fullWeather)
    }

    @Test(expected = IOException::class)
    fun fetchDataForSingleLocation_failedWeatherApi_invalidReturn() {
        // Arrange

        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                lat.toString(),
                long.toString()
            )
        } throws IOException("Unable fetch data")

        // Assert
        runBlocking { weatherSource.getWeather(latlon) }
    }

    @Test(expected = IOException::class)
    fun fetchDataForSingleLocation_failedLocation_invalidReturn() {
        // Arrange

        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                lat.toString(),
                long.toString()
            )
        } returns weatherResponse
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long
            )
        }.throws(IOException())

        // Assert
        runBlocking { weatherSource.getWeather(latlon) }
    }

    @Test
    fun searchAboveFallbackTime_validLocation_validReturn() {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)

        // Act
        coEvery { repository.getSingleWeather(CURRENT_LOCATION) }.returns(entityItem)
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) }.returns(Unit)
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(false)

        // Assert
        val result = runBlocking { weatherSource.getWeather(latlon) }
        assertEquals(result, fullWeather)
    }

    @Test
    fun forceFetchDataForSingleLocation_validLocation_validReturn() {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)

        // Act
        coEvery { repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION) } returns entityItem
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.latitude.toString(),
                weatherResponse.longitude.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        every { repository.getUnitType() } returns UnitType.METRIC
        every { repository.saveLastSavedAt(CURRENT_LOCATION) } returns Unit
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) } returns Unit

        // Assert
        val result = runBlocking {
            weatherSource.forceFetchWeather(
                latlon,
                locationType = LocationType.City
            )
        }
        assertEquals(result, fullWeather)
    }
}