package com.appttude.h_mal.atlas_weather.testSuite

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.Converter
import com.appttude.h_mal.atlas_weather.data.room.WeatherDao
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.getOrAwaitValue
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import java.util.UUID


class RoomDatabaseTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .addTypeConverter(Converter(context))
            .build()
        dao = db.getWeatherDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenInitializedThenListIsEmpty() {
        assertTrue(dao.getAllFullWeatherWithoutCurrent().getOrAwaitValue().isEmpty())
    }

    @Test
    fun whenElementAddedThenItemIsInDatabase() {
        // Arrange
        val item = createEntity()
        // Act
        dao.upsertFullWeather(item)
        // Assert
        assertEquals(item, dao.getCurrentFullWeather(item.id).getOrAwaitValue())
    }

    @Test
    fun whenCurrentElementAddedThenLiveDataIsEmpty() {
        // Arrange
        val item = createEntity()
        // Act
        dao.upsertFullWeather(item)
        // Assert
        assertEquals(0, dao.getAllFullWeatherWithoutCurrent(CURRENT_LOCATION).getOrAwaitValue().size)
    }

    @Test
    fun whenNewElementAddedThenLiveDataIsNot() {
        // Arrange
        val elements = createEntityList()
        val id = elements.first().id
        // Act
        dao.upsertListOfFullWeather(elements)
        // Assert
        assertEquals(elements.size - 1, dao.getAllFullWeatherWithoutCurrent(id).getOrAwaitValue().size)
    }

    @Test
    fun wheElementDeletedThenContainsIsFalse() {
        // Arrange
        val elements = createEntityList()
        val id = elements.first().id
        // Act
        dao.upsertListOfFullWeather(elements)
        dao.deleteEntry(id)
        // Assert
        assertEquals(elements.size - 1, dao.getAllFullWeatherWithoutCurrent(id).getOrAwaitValue().size)
        assertNull(dao.getCurrentFullWeatherSingle(id))
    }

    private fun createEntity(id: String = CURRENT_LOCATION): EntityItem {
        val weather = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            FullWeather()
        } else {
            mockk<FullWeather>()
        }
        return EntityItem(id, weather)
    }

    private fun createEntityList(size: Int = 4): List<EntityItem> {
        return (0.. size).map {
            val id = UUID.randomUUID().toString()
            createEntity(id)
        }
    }

}