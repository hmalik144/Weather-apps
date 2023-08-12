package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.prefs.LOCATION_CONST
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.utils.BaseTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

private const val MORE_THAN_FIVE_MINS = 330000L
private const val LESS_THAN_FIVE_MINS = 270000L

class RepositoryImplTest : BaseTest() {

    lateinit var repository: RepositoryImpl

    @MockK
    lateinit var api: WeatherApi

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
        val mockResponse = createSuccessfulRetrofitMock<WeatherResponse>()

        //Act
        //create a successful retrofit response
        coEvery { api.getFromApi("", "") }.returns(mockResponse)

        // Assert
        runBlocking {
            val result = repository.getWeatherFromApi("", "")
            assertIs<WeatherResponse>(result)
        }
    }

    @Test
    fun getWeatherFromApi_validLatLong_invalidResponse() {
        //Arrange
        val mockResponse = createErrorRetrofitMock<WeatherResponse>()

        //Act
        //create a successful retrofit response
        coEvery { api.getFromApi(any(), any()) } returns (mockResponse)

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