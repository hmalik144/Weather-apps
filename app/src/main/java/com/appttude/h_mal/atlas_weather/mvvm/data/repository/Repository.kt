package com.appttude.h_mal.atlas_weather.mvvm.data.repository

import androidx.lifecycle.LiveData
import com.appttude.h_mal.atlas_weather.mvvm.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.mvvm.data.room.entity.EntityItem

interface Repository {

    suspend fun getWeatherFromApi(lat: String, long: String): WeatherResponse
    suspend fun saveCurrentWeatherToRoom(locationId: String, weatherResponse: WeatherResponse)
    suspend fun saveWeatherListToRoom(list: List<EntityItem>)
    fun loadAllWeatherExceptCurrentFromRoom(): LiveData<List<EntityItem>>
    fun loadCurrentWeatherFromRoom(id: String): LiveData<EntityItem>
    fun isSearchValid(locationName: String): Boolean
    fun saveLastSavedAt(locationName: String)
    suspend fun deleteSavedWeatherEntry(locationName: String)
    fun getSavedLocations(): List<String>
}