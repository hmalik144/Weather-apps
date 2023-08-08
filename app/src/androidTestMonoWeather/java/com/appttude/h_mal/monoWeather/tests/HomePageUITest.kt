package com.appttude.h_mal.monoWeather.tests


00import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.monoWeather.MonoBaseTest
import com.appttude.h_mal.monoWeather.robot.homeScreen
import org.junit.Test
import java.io.IOException

class HomePageUITest : MonoBaseTest() {

    override fun beforeLaunch() {
        stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Valid)
    }

    @Test
    fun loadApp_validWeatherResponse_returnsValidPage() {
        homeScreen {
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
        }
        throw IOException("sdfsdfasdfasdfasdf")
    }
}
