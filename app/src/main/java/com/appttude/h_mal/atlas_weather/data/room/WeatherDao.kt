package com.appttude.h_mal.atlas_weather.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertFullWeather(item: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertListOfFullWeather(items: List<WeatherEntity>)

    @Query("SELECT * FROM WeatherEntity WHERE id = :userId LIMIT 1")
    fun getCurrentFullWeather(userId: String): LiveData<WeatherEntity>

    @Query("SELECT * FROM WeatherEntity WHERE id = :userId LIMIT 1")
    fun getCurrentFullWeatherSingle(userId: String): WeatherEntity

    @Query("SELECT * FROM WeatherEntity WHERE id != :id")
    fun getAllFullWeatherWithoutCurrent(id: String = CURRENT_LOCATION): LiveData<List<WeatherEntity>>

    @Query("SELECT * FROM WeatherEntity WHERE id != :id")
    fun getWeatherListWithoutCurrent(id: String = CURRENT_LOCATION): List<WeatherEntity>

    @Query("DELETE FROM WeatherEntity WHERE id = :userId")
    fun deleteEntry(userId: String): Int

}