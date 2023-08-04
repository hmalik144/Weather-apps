package com.appttude.h_mal.atlas_weather.tests


import com.appttude.h_mal.atlas_weather.robot.homeScreen
import com.appttude.h_mal.atlas_weather.testsuite.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import org.junit.Test

class HomePageUITest : BaseTest<MainActivity>() {

    override fun setupFeed() {
        stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Valid)
    }

    @Test
    fun loadApp_validWeatherResponse_returnsValidPage() {
        homeScreen {
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
        }
    }
}
