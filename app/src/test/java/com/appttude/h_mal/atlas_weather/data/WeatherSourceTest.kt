package com.appttude.h_mal.atlas_weather.data

import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
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
import kotlin.test.assertEquals

class WeatherSourceTest : BaseTest() {

    @InjectMockKs
    lateinit var weatherSource: WeatherSource

    @MockK
    lateinit var repository: RepositoryImpl

    @MockK
    lateinit var locationProvider: LocationProviderImpl

    private lateinit var weatherResponse: WeatherResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        weatherResponse = getTestData("weather_sample.json", WeatherResponse::class.java)
    }

    @Test
    fun fetchDataForSingleLocation_validLocation_validReturn() {
        // Arrange
        val latlon = Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )
        val fullWeather = FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        }
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)


        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                weatherResponse.lat,
                weatherResponse.lon,
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
        val latlon = Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )

        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        } throws IOException("Unable fetch data")

        // Assert
        runBlocking { weatherSource.getWeather(latlon) }
    }

    @Test(expected = IOException::class)
    fun fetchDataForSingleLocation_failedLocation_invalidReturn() {
        // Arrange
        val latlon = Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )

        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        } returns weatherResponse
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                weatherResponse.lat,
                weatherResponse.lon
            )
        }.throws(IOException())

        // Assert
        runBlocking { weatherSource.getWeather(latlon) }
    }

    @Test
    fun searchAboveFallbackTime_validLocation_validReturn() {
        // Arrange
        val latlon = Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )
        val fullWeather = FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        }
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
        val latlon = Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )
        val fullWeather = FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        }
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)

        // Act
        coEvery { repository.loadSingleCurrentWeatherFromRoom(CURRENT_LOCATION) } returns entityItem
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                weatherResponse.lat,
                weatherResponse.lon,
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