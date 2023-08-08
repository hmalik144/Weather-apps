package com.appttude.h_mal.atlas_weather.helper

import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ServicesHelperTest {

    private val gson = Gson()

    lateinit var helper: ServicesHelper

    @MockK
    lateinit var repository: Repository

    @MockK
    lateinit var settingsRepository: SettingsRepository

    @MockK
    lateinit var locationProvider: LocationProviderImpl

    lateinit var weatherResponse: WeatherResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = ServicesHelper(repository, settingsRepository, locationProvider)

        val json = this::class.java.classLoader!!.getResource("weather_sample.json").readText()
        weatherResponse = gson.fromJson(json, WeatherResponse::class.java)
    }

    @Test
    fun testWidgetDataAsync_successfulResponse() = runBlocking {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        })

        // Act
        coEvery { locationProvider.getCurrentLatLong() } returns Pair(
            weatherResponse.lat,
            weatherResponse.lon
        )
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                weatherResponse.lat,
                weatherResponse.lon
            )
        }.returns(CURRENT_LOCATION)
        every { repository.saveLastSavedAt(CURRENT_LOCATION) } returns Unit
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) } returns Unit

        // Assert
        val result = helper.fetchData()
        assertTrue(result)
    }

    @Test
    fun testWidgetDataAsync_unsuccessfulResponse() = runBlocking {
        // Act
        coEvery { locationProvider.getCurrentLatLong() } returns Pair(0.0, 0.0)
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery { repository.getWeatherFromApi("0.0", "0.0") } throws IOException("error")

        // Assert
        val result = helper.fetchData()
        assertTrue(!result)
    }

    @Test
    fun testWidgetDataAsync_invalidSearch() = runBlocking {
        // Act
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(false)

        // Assert
        val result = helper.fetchData()
        assertTrue(!result)
    }
}