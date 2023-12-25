package com.appttude.h_mal.atlas_weather.data.prefs

import androidx.test.core.app.ApplicationProvider
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PreferenceProviderTest {

    lateinit var preferenceProvider: PreferenceProvider

    @Before
    fun setUp() {
        preferenceProvider = PreferenceProvider(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun saveAndGetLastSavedAt() {
        // Arrange
        val input = anyString()

        // Act
        preferenceProvider.saveLastSavedAt(input)

        // Assert
        val result = preferenceProvider.getLastSavedAt(input)
        runBlocking { sleep(100) }
        assert(result < System.currentTimeMillis())
    }

    @Test
    fun getAllKeysAndDeleteKeys() {
        // Arrange
        val listOfLocations = listOf(CURRENT_LOCATION, "sydney", "london", "berlin", "dublin")

        // Act
        listOfLocations.forEach { preferenceProvider.saveLastSavedAt(it) }

        // Assert
        val result = preferenceProvider.getAllKeysExcludingCurrent()
        assert(result.size > 0)
        assertFalse { result.contains(CURRENT_LOCATION) }

        // Act
        listOfLocations.forEach{ preferenceProvider.deleteLocation(it)}

        // Assert
        val deletedResults = preferenceProvider.getAllKeysExcludingCurrent()
        assertFalse { deletedResults.containsAll(listOfLocations) }
    }

    @Test
    fun setAndGetFirstTimeRun() {
        // Act
        preferenceProvider.setFirstTimeRun()
        runBlocking { sleep(100) }

        // Assert
        val result = preferenceProvider.getFirstTimeRun()
        assertFalse(result)
    }

    @Test
    fun setAndGetUnitsType() {
        // Arrange
        val input = UnitType.values()[kotlin.random.Random.nextInt(UnitType.values().size)]

        // Act
        preferenceProvider.setUnitsType(input)

        // Assert
        val result = preferenceProvider.getUnitsType()
        assertEquals(result, input)
    }
}