package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.network.NewWeatherApi
import com.appttude.h_mal.atlas_weather.data.network.response.weather.WeatherApiResponse
import com.appttude.h_mal.atlas_weather.data.prefs.LOCATION_CONST
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import com.nhaarman.mockitokotlin2.any
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyDouble
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

private const val MORE_THAN_FIVE_MINS = 330000L
private const val LESS_THAN_FIVE_MINS = 270000L

class RepositoryImplTest : BaseTest() {

    lateinit var repository: RepositoryImpl

    @MockK lateinit var api: NewWeatherApi

    @MockK
    lateinit var db: AppDatabase

    @MockK
    lateinit var prefs: PreferenceProvider

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RepositoryImpl(api, db, prefs)
    }

    @Test
    fun isSearchValid_lessThanFiveMinutes_invalidSearch() {
        //Arrange
        val location = "CurrentLocation"

        //Act
        every { prefs.getLastSavedAt("$LOCATION_CONST$location") } returns (System.currentTimeMillis() - LESS_THAN_FIVE_MINS)

        //Assert
        val valid: Boolean = repository.isSearchValid(location)
        assertEquals(valid, false)
    }

    @Test
    fun isSearchValid_moreThanFiveMinutes_validSearch() {
        //Arrange
        val location = "CurrentLocation"

        //Act
        every { prefs.getLastSavedAt("$LOCATION_CONST$location") } returns (System.currentTimeMillis() - MORE_THAN_FIVE_MINS)

        // Assert
        val valid: Boolean = repository.isSearchValid(location)
        assertEquals(valid, true)
    }

    @Test
    fun isSearchValid_noPreviousValue_validSearch() {
        //Arrange
        val location = "CurrentLocation"

        //Act
        every { prefs.getLastSavedAt("$LOCATION_CONST$location") } returns 0L

        // Assert
        val valid: Boolean = repository.isSearchValid(location)
        assertEquals(valid, true)
    }

    @Test
    fun getWeatherFromApi_validLatLong_validSearch() {
        //Arrange
        val lat = anyDouble().toString()
        val long = anyDouble().toString()
        val mockResponse = createSuccessfulRetrofitMock<WeatherApiResponse>()

        //Act
        //create a successful retrofit response
        every { prefs.getUnitsType() } returns (UnitType.METRIC)
        coEvery { api.getFromApi(location = lat + long) }.returns(mockResponse)

        // Assert
        runBlocking {
            val result = repository.getWeatherFromApi(lat, long)
            assertIs<WeatherApiResponse>(result)
        }
    }

    @Test
    fun getWeatherFromApi_validLatLong_invalidResponse() {
        //Arrange
        val mockResponse = createErrorRetrofitMock<WeatherApiResponse>()

        //Act
        //create a successful retrofit response
        every { prefs.getUnitsType() } returns (UnitType.METRIC)
        coEvery { api.getFromApi(location = any()) } returns (mockResponse)

        // Assert
        val ioExceptionReturned = assertFailsWith<IOException> {
            runBlocking {
                repository.getWeatherFromApi("", "")
            }
        }
        assertEquals(ioExceptionReturned.message, "Error Code: 400")
    }

    @Test
    fun loadWeatherList_validResponse() {
        // Arrange
        val elements = listOf<EntityItem>(
            mockk { every { id } returns any() },
            mockk { every { id } returns any() },
            mockk { every { id } returns any() },
            mockk { every { id } returns any() }
        )

        //Act
        coEvery { db.getWeatherDao().getWeatherListWithoutCurrent() } returns elements

        // Assert
        runBlocking {
            val result = repository.loadWeatherList()
            assertIs<List<String>>(result)
        }
    }


}