package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.ViewState
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import com.appttude.h_mal.atlas_weather.utils.getOrAwaitValue
import com.appttude.h_mal.atlas_weather.utils.sleep
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertIs


class WorldViewModelTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: WorldViewModel

    @MockK(relaxed = true)
    lateinit var repository: Repository

    @MockK
    lateinit var locationProvider: LocationProviderImpl

    lateinit var weatherResponse: WeatherResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = WorldViewModel(locationProvider, repository)

        weatherResponse = getTestData("weather_sample.json", WeatherResponse::class.java)
    }

    @Test
    fun fetchDataForSingleLocation_validLocation_validReturn() {
        // Arrange
        val location = CURRENT_LOCATION
        val entityItem = EntityItem(CURRENT_LOCATION, FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        })

        // Act
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns Pair(
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
                weatherResponse.lon,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        every { repository.saveLastSavedAt(CURRENT_LOCATION) } returns Unit
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) } returns Unit

        viewModel.fetchDataForSingleLocation(location)

        // Assert
        sleep(300)
        assertIs<ViewState.HasData<*>>(viewModel.uiState.getOrAwaitValue())
    }

    @Test
    fun fetchDataForSingleLocation_invalidLocation_invalidReturn() {
        // Arrange
        val location = CURRENT_LOCATION

        // Act
        every { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } throws IOException("Unable to get location")
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(true)
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        }.returns(weatherResponse)

        viewModel.fetchDataForSingleLocation(location)

        // Assert
        sleep(300)
        assertIs<ViewState.HasError<*>>(viewModel.uiState.getOrAwaitValue())
    }

    @Test
    fun searchAboveFallbackTime_validLocation_validReturn() {
        // Arrange
        val entityItem = EntityItem(CURRENT_LOCATION, FullWeather(weatherResponse).apply {
            temperatureUnit = "°C"
            locationString = CURRENT_LOCATION
        })

        // Act
        coEvery { repository.getSingleWeather(CURRENT_LOCATION) }.returns(entityItem)
        every { repository.isSearchValid(CURRENT_LOCATION) }.returns(false)
        coEvery {
            repository.getWeatherFromApi(
                weatherResponse.lat.toString(),
                weatherResponse.lon.toString()
            )
        }.returns(weatherResponse)
        every { repository.saveLastSavedAt(CURRENT_LOCATION) } returns Unit
        coEvery { repository.saveCurrentWeatherToRoom(entityItem) } returns Unit

        viewModel.fetchDataForSingleLocation(CURRENT_LOCATION)

        // Assert
        sleep(300)
        assertIs<ViewState.HasData<*>>(viewModel.uiState.getOrAwaitValue())
    }
}