package com.appttude.h_mal.atlas_weather.data.repository

import androidx.lifecycle.LiveData
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem

interface Repository {

    suspend fun getWeatherFromApi(lat: String, long: String): WeatherResponse
    suspend fun saveCurrentWeatherToRoom(entityItem: EntityItem)
    suspend fun saveWeatherListToRoom(list: List<EntityItem>)
    fun loadRoomWeatherLiveData(): LiveData<List<EntityItem>>
    suspend fun loadWeatherList(): List<String>
    fun loadCurrentWeatherFromRoom(id: String): LiveData<EntityItem>
    suspend fun loadSingleCurrentWeatherFromRoom(id: String): EntityItem
    fun isSearchValid(locationName: String): Boolean
    fun saveLastSavedAt(locationName: String)
    suspend fun deleteSavedWeatherEntry(locationName: String): Boolean
    fun getSavedLocations(): List<String>
    suspend fun getSingleWeather(locationName: String): EntityItem
}