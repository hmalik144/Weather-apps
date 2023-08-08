package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.prefs.LOCATION_CONST
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private const val MORE_THAN_FIVE_MINS = 330000L
private const val LESS_THAN_FIVE_MINS = 270000L

class RepositoryImplTest {

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
}