package com.appttude.h_mal.atlas_weather.data.repository

import com.appttude.h_mal.atlas_weather.data.network.ResponseUnwrap
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.prefs.LOCATION_CONST
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.utils.FALLBACK_TIME


class RepositoryImpl(
    private val api: WeatherApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : Repository, ResponseUnwrap() {

    override suspend fun getWeatherFromApi(
        lat: String,
        long: String
    ): WeatherResponse {
        return responseUnwrap { api.getFromApi(lat, long, units = prefs.getUnitsType().name.lowercase()) }
    }

    override suspend fun saveCurrentWeatherToRoom(entityItem: EntityItem) {
        db.getWeatherDao().upsertFullWeather(entityItem)
    }

    override suspend fun saveWeatherListToRoom(
        list: List<EntityItem>
    ) {
        db.getWeatherDao().upsertListOfFullWeather(list)
    }

    override fun loadRoomWeatherLiveData() = db.getWeatherDao().getAllFullWeatherWithoutCurrent()

    override suspend fun loadWeatherList(): List<String> {
        return db.getWeatherDao()
            .getWeatherListWithoutCurrent()
            .map { it.id }
    }

    override fun loadCurrentWeatherFromRoom(id: String) =
        db.getWeatherDao().getCurrentFullWeather(id)

    override suspend fun loadSingleCurrentWeatherFromRoom(id: String) =
        db.getWeatherDao().getCurrentFullWeatherSingle(id)

    override fun isSearchValid(locationName: String): Boolean {
        val lastSaved = prefs
            .getLastSavedAt("$LOCATION_CONST$locationName")
        val difference = System.currentTimeMillis() - lastSaved

        return difference > FALLBACK_TIME
    }

    override fun saveLastSavedAt(locationName: String) {
        prefs.saveLastSavedAt("$LOCATION_CONST$locationName")
    }

    override suspend fun deleteSavedWeatherEntry(locationName: String): Boolean {
        prefs.deleteLocation(locationName)
        return db.getWeatherDao().deleteEntry(locationName) > 0
    }

    override fun getSavedLocations(): List<String> {
        return prefs.getAllKeysExcludingCurrent().toList()
    }

    override suspend fun getSingleWeather(locationName: String): EntityItem {
        return db.getWeatherDao().getCurrentFullWeatherSingle(locationName)
    }

    override fun getUnitType(): UnitType {
        return prefs.getUnitsType()
    }

}