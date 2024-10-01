package com.appttude.h_mal.atlas_weather.helper

import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.weather.WeatherApiResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.properties.Delegates

class ServicesHelperTest : BaseTest() {

    lateinit var helper: ServicesHelper

    @MockK
    lateinit var repository: Repository

    @MockK
    lateinit var settingsRepository: SettingsRepository

    @MockK
    lateinit var locationProvider: LocationProviderImpl

    lateinit var weatherResponse: WeatherApiResponse
    private var lat by Delegates.notNull<Double>()
    private var long by Delegates.notNull<Double>()
    private lateinit var latlon: Pair<Double, Double>
    private lateinit var fullWeather: FullWeather

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = ServicesHelper(repository, settingsRepository, locationProvider)

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
    fun testWidgetDataAsync_successfulResponse() = runBlocking {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)

        // Act
        coEvery { locationProvider.getCurrentLatLong() } returns Pair(lat, long)
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                lat.toString(),
                long.toString()
            )
        }.returns(weatherResponse)
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long
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