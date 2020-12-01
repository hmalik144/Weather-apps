package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.network.ResponseUnwrap
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.prefs.LOCATION_CONST
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather

private const val FIVE_MINS = 300000L
class RepositoryImpl(
    private val api: WeatherApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : Repository, ResponseUnwrap() {

    override suspend fun getWeatherFromApi(
        lat: String,
        long: String
    ): WeatherResponse {
        return responseUnwrap { api.getFromApi(lat, long) }
    }

    override suspend fun saveCurrentWeatherToRoom(
            locationId: String,
            weatherResponse: WeatherResponse
    ){
        val entity = EntityItem(
                locationId,
                FullWeather(weatherResponse)
        )
        db.getSimpleDao().upsertFullWeather(entity)
    }

    override suspend fun saveWeatherListToRoom(
            list: List<EntityItem>
    ){
        db.getSimpleDao().upsertListOfFullWeather(list)
    }

    override fun loadAllWeatherExceptCurrentFromRoom() = db.getSimpleDao().getAllFullWeatherWithoutCurrent()

    override fun loadCurrentWeatherFromRoom(id: String) = db.getSimpleDao().getCurrentFullWeather(id)

    override suspend fun loadSingleCurrentWeatherFromRoom(id: String) = db.getSimpleDao().getCurrentFullWeatherSingle(id)

    override fun isSearchValid(locationName: String): Boolean {
        val lastSaved = prefs.getLastSavedAt(locationName) ?: return true
        val difference = System.currentTimeMillis() - lastSaved
        return difference > FIVE_MINS
    }

    override fun saveLastSavedAt(locationName: String) {
        prefs.saveLastSavedAt("$LOCATION_CONST$locationName")
    }

    override suspend fun deleteSavedWeatherEntry(locationName: String): Boolean {
        prefs.deleteLocation(locationName)
        return db.getSimpleDao().deleteEntry(locationName) > 0
    }

    override fun getSavedLocations(): List<String> {
        return prefs.getAllKeys().toList()
    }

}