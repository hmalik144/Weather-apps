package com.appttude.h_mal.monoWeather.tests


import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HomePageNoDataUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.InvalidKey, 400, ".txt")
    }

    @Test
    fun invalidKeyWeatherResponse_swipeToRefresh_returnsValidPage() {
        weatherScreen {
            // verify empty
            verifyUnableToRetrieve()

            stubEndpoint(baseUrl, Stubs.Metric)
            refresh()
            verifyCurrentTemperature(13)
            verifyCurrentLocation("Mock Location")
        }
    }

}
