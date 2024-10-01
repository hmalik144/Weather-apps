package com.appttude.h_mal.atlas_weather.tests

import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.robot.homeScreen
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import org.junit.Test

class HomePageNoDataUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.InvalidKey, 400, ".txt")
    }

    @Test
    fun invalidKeyWeatherResponse_swipeToRefresh_returnsValidPage() {
        homeScreen {
            // verify empty
            verifyUnableToRetrieve()

            stubEndpoint(baseUrl, Stubs.Metric)
            refresh()
            verifyCurrentTemperature(13)
            verifyCurrentLocation("Mock Location")
        }
    }
}
