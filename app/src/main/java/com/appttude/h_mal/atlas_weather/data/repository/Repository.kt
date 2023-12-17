package com.appttude.h_mal.atlas_weather.data.repository

import androidx.lifecycle.LiveData
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.room.entity.WeatherEntity

interface Repository {

    suspend fun getWeatherFromApi(lat: String, long: String): WeatherResponse
    suspend fun saveCurrentWeatherToRoom(weatherEntity: WeatherEntity)
    suspend fun saveWeatherListToRoom(list: List<WeatherEntity>)
    fun loadRoomWeatherLiveData(): LiveData<List<WeatherEntity>>
    suspend fun loadWeatherList(): List<String>
    fun loadCurrentWeatherFromRoom(id: String): LiveData<WeatherEntity>
    suspend fun loadSingleCurrentWeatherFromRoom(id: String): WeatherEntity
    fun isSearchValid(locationName: String): Boolean
    fun saveLastSavedAt(locationName: String)
    suspend fun deleteSavedWeatherEntry(locationName: String): Boolean
    fun getSavedLocations(): List<String>
    suspend fun getSingleWeather(locationName: String): WeatherEntity
}