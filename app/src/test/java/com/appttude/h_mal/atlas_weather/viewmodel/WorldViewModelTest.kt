package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.response.weather.WeatherApiResponse
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.ViewState
import com.appttude.h_mal.atlas_weather.model.types.LocationType
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import com.appttude.h_mal.atlas_weather.utils.getOrAwaitValue
import com.appttude.h_mal.atlas_weather.utils.sleep
import com.nhaarman.mockitokotlin2.any
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import java.io.IOException
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertIs


class WorldViewModelTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @InjectMockKs
    lateinit var viewModel: WorldViewModel

    @RelaxedMockK
    lateinit var weatherSource: WeatherSource

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
        val latlon = any<Pair<Double, Double>>()

        // Act
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns Pair(
            lat,
            long
        )
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        coEvery {
            weatherSource.getWeather(
                latlon,
                CURRENT_LOCATION,
                locationType = LocationType.City
            )
        } returns fullWeather

        // Assert
        viewModel.fetchDataForSingleLocation(CURRENT_LOCATION)

        sleep(100)
        assertIs<ViewState.HasData<*>>(viewModel.uiState.getOrAwaitValue())
    }

    @Test
    fun fetchDataForSingleLocation_failedLocation_validReturn() {
        // Arrange
        val errorMessage = anyString()

        // Act
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns Pair(
            lat,
            long
        )
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        } throws IOException(errorMessage)

        // Assert
        viewModel.fetchDataForSingleLocation(CURRENT_LOCATION)
        sleep(100)
        val observerResults = viewModel.uiState.getOrAwaitValue()
        assertIs<ViewState.HasError<*>>(observerResults)
        assertEquals(observerResults.error as String, errorMessage)
    }

    @Test
    fun fetchDataForSingleLocation_failedApi_validReturn() {
        // Arrange
        val errorMessage = anyString()

        // Act
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns Pair(
            lat,
            long
        )
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        coEvery {
            weatherSource.getWeather(
                latlon,
                CURRENT_LOCATION,
                locationType = LocationType.City
            )
        } throws IOException(errorMessage)

        // Assert
        viewModel.fetchDataForSingleLocation(CURRENT_LOCATION)
        sleep(100)
        val observerResults = viewModel.uiState.getOrAwaitValue()
        assertIs<ViewState.HasError<*>>(observerResults)
        assertEquals(observerResults.error as String, errorMessage)
    }

    @Test
    fun fetchDataForSingleLocationSearch_validLocation_validReturn() {
        // Arrange

        // Act
        every { weatherSource.repository.getSavedLocations() } returns anyList()
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat,
                long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        coEvery {
            weatherSource.getWeather(
                latlon,
                CURRENT_LOCATION,
                locationType = LocationType.City
            )
        } returns fullWeather.apply { locationString = CURRENT_LOCATION }

        // Assert
        viewModel.fetchDataForSingleLocationSearch(CURRENT_LOCATION)

        sleep(100)
        val result = viewModel.uiState.getOrAwaitValue()
        assertIs<ViewState.HasData<*>>(result)
        assertEquals(result.data as String, "$CURRENT_LOCATION saved")
    }

    @Test
    fun fetchDataForSingleLocationSearch_locationAlreadyExists_errorReceived() {
        // Act
        every { weatherSource.repository.getSavedLocations() } returns listOf(CURRENT_LOCATION)

        // Assert
        viewModel.fetchDataForSingleLocationSearch(CURRENT_LOCATION)

        sleep(100)
        val result = viewModel.uiState.getOrAwaitValue()
        assertIs<ViewState.HasError<*>>(result)
        assertEquals(result.error as String, "$CURRENT_LOCATION already exists")
    }

    @Test
    fun fetchDataForSingleLocationSearch_retrievedLocationExists_validError() {
        // Arrange
        val retrievedLocation = anyString()

        // Act
        every { weatherSource.repository.getSavedLocations() } returns listOf(retrievedLocation)
        coEvery { locationProvider.getLatLongFromLocationName(CURRENT_LOCATION) } returns latlon
        coEvery {
            locationProvider.getLocationNameFromLatLong(
                lat, long,
                LocationType.City
            )
        }.returns(CURRENT_LOCATION)
        coEvery {
            weatherSource.getWeather(
                latlon,
                CURRENT_LOCATION,
                locationType = LocationType.City
            )
        } returns fullWeather.apply { locationString = retrievedLocation }

        // Assert
        viewModel.fetchDataForSingleLocationSearch(CURRENT_LOCATION)

        sleep(100)
        val result = viewModel.uiState.getOrAwaitValue()
        assertIs<ViewState.HasError<*>>(result)
        assertEquals(result.error as String, "$retrievedLocation already exists")
    }

    @Test
    fun fetchAllLocations_validLocations_validReturn() {
        // Arrange
        val listOfPlaces = listOf("Sydney", "London", "Cairo")

        // Act
        listOfPlaces.forEachIndexed { index, s ->
            every { weatherSource.repository.isSearchValid(s) } returns true
            coEvery { locationProvider.getLatLongFromLocationName(s) } returns Pair(
                index.toDouble(),
                index.toDouble()
            )
            coEvery {
                locationProvider.getLocationNameFromLatLong(
                    index.toDouble(),
                    index.toDouble(),
                    LocationType.City
                )
            }.returns(s)
            coEvery {
                weatherSource.getWeather(
                    Pair(index.toDouble(), index.toDouble()),
                    s,
                    LocationType.City
                )
            }
        }
        coEvery { weatherSource.repository.loadWeatherList() } returns listOfPlaces

        // Assert
        viewModel.fetchAllLocations()

        sleep(100)
        assertIs<ViewState.HasData<*>>(viewModel.uiState.getOrAwaitValue())
    }

    @Test
    fun deleteLocation_validLocations_validReturn() {
        // Arrange
        val location = anyString()

        // Act
        coEvery { weatherSource.repository.deleteSavedWeatherEntry(location) } returns true

        // Assert
        viewModel.deleteLocation(location)

        sleep(100)
        assertIs<ViewState.HasData<*>>(viewModel.uiState.getOrAwaitValue())
    }

}