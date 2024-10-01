package com.appttude.h_mal.monoWeather.tests


import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import com.appttude.h_mal.monoWeather.robot.furtherInfoScreen
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import org.junit.Test

class HomePageUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.Metric)
        clearPrefs()
    }

    @Test
    fun loadApp_validWeatherResponse_returnsValidPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(13)
            verifyCurrentLocation("Mock Location")
        }
    }

    @Test
    fun loadApp_validWeatherResponse_viewFurtherDetailsPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(13)
            verifyCurrentLocation("Mock Location")
            tapDayInformationByPosition(4)
        }
        furtherInfoScreen {
            isDisplayed()
            verifyMaxTemperature(15)
            verifyAverageTemperature(11)
        }
    }

}
