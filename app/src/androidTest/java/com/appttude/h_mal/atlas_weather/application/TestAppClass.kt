package com.appttude.h_mal.atlas_weather.application

import androidx.room.Room
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import com.appttude.h_mal.atlas_weather.data.location.MockLocationProvider
import com.appttude.h_mal.atlas_weather.data.network.NetworkModule
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.MockingNetworkInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.data.room.Converter
import java.io.BufferedReader

class TestAppClass : BaseAppClass() {
    private val idlingResources = CountingIdlingResource("Data_loader")
    private val mockingNetworkInterceptor = MockingNetworkInterceptor(idlingResources)

    override fun onCreate() {
        super.onCreate()
        IdlingRegistry.getInstance().register(idlingResources)
    }

    override fun createNetworkModule(): WeatherApi {
        return NetworkModule().invoke<WeatherApi>(
                mockingNetworkInterceptor,
                NetworkConnectionInterceptor(this),
                QueryParamsInterceptor(),
                loggingInterceptor
        ) as WeatherApi
    }

    override fun createLocationModule() = MockLocationProvider()

    override fun createRoomDatabase(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(this, AppDatabase::class.java)
                .addTypeConverter(Converter(this))
                .build()
    }

    fun stubUrl(url: String, rawPath: String) {
        val id = resources.getIdentifier(rawPath, "raw", packageName)
        val iStream = resources.openRawResource(id)
        val data = iStream.bufferedReader().use(BufferedReader::readText)
        mockingNetworkInterceptor.addUrlStub(url = url, data = data)
    }

    fun removeUrlStub(url: String) {
        mockingNetworkInterceptor.removeUrlStub(url = url)
    }

}