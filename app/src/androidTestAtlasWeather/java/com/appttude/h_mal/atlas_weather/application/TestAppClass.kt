package com.appttude.h_mal.atlas_weather.application

import androidx.room.Room
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.location.MockLocationProvider
import com.appttude.h_mal.atlas_weather.data.network.NetworkModule
import com.appttude.h_mal.atlas_weather.data.network.NewWeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.MockingNetworkInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.Converter
import java.io.BufferedReader

class TestAppClass : AppClass() {
    private val idlingResources = CountingIdlingResource("Data_loader")
    private val mockingNetworkInterceptor = MockingNetworkInterceptor(idlingResources)

    lateinit var database: AppDatabase
    private val locationProvider: MockLocationProvider = MockLocationProvider()

    override fun onCreate() {
        super.onCreate()
        IdlingRegistry.getInstance().register(idlingResources)
    }

    override fun createNetworkModule(): NewWeatherApi {
        return NetworkModule().invoke<NewWeatherApi>(
            mockingNetworkInterceptor,
            NetworkConnectionInterceptor(this),
            QueryParamsInterceptor(),
            loggingInterceptor
        ) as WeatherApi
    }

    override fun createLocationModule(): LocationProvider {
        return locationProvider
    }

    override fun createRoomDatabase(): AppDatabase {
        database = Room.inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .addTypeConverter(Converter(this))
            .build()
        return database
    }

    fun stubUrl(url: String, rawPath: String, code: Int = 200) {
        val iStream =
            InstrumentationRegistry.getInstrumentation().context.assets.open("$rawPath.json")
        val data = iStream.bufferedReader().use(BufferedReader::readText)
        mockingNetworkInterceptor.addUrlStub(url = url, data = data, code = code)
    }

    fun removeUrlStub(url: String) {
        mockingNetworkInterceptor.removeUrlStub(url = url)
    }

    fun stubLocation(location: String, lat: Double = 0.00, long: Double = 0.00) {
        locationProvider.addLocationToList(location, lat, long)
    }

    fun removeLocation(location: String) {
        locationProvider.removeLocationFromList(location)
    }

    fun removeLocation(lat: Double, long: Double) {
        locationProvider.removeLocationFromList(lat, long)
    }

    fun clearDatabase() {
        database.getWeatherDao().deleteAll()
    }
}