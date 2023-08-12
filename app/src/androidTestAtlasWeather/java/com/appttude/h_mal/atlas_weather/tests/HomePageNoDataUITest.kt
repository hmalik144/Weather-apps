package com.appttude.h_mal.atlas_weather.tests

import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.robot.homeScreen
import org.junit.Test

class HomePageNoDataUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.InvalidKey, 400)
    }

    @Test
    fun loadApp_invalidKeyWeatherResponse_returnsEmptyViewPage() {
        homeScreen {
            // verify empty
            verifyUnableToRetrieve()
        }
    }

    @Test
    fun invalidKeyWeatherResponse_swipeToRefresh_returnsValidPage() {
        homeScreen {
            // verify empty
            verifyUnableToRetrieve()

            stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Metric)
            refresh()
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
        }
    }
}
