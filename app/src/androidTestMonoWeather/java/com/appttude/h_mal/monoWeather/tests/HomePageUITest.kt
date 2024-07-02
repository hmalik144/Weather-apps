package com.appttude.h_mal.monoWeather.tests


import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.monoWeather.robot.furtherInfoScreen
import com.appttude.h_mal.monoWeather.robot.settingsScreen
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import org.junit.Test
import tools.fastlane.screengrab.Screengrab

class HomePageUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Metric)
        clearPrefs()
    }

    @Test
    fun loadApp_validWeatherResponse_returnsValidPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
        }
    }

    @Test
    fun loadApp_validWeatherResponse_viewFurtherDetailsPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
            tapDayInformationByPosition(4)
        }
        furtherInfoScreen {
            isDisplayed()
            verifyMaxTemperature(12)
            verifyAverageTemperature(9)
        }

    }

    @Test
    fun loadApp_changeToImperial_returnsValidPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(2)
            verifyCurrentLocation("Mock Location")
            stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Imperial)
            openMenuItem()
        }
        settingsScreen {
            selectWeatherUnits(UnitType.IMPERIAL)
            goBack()
        }
        weatherScreen {
            isDisplayed()
            refresh()
            verifyCurrentTemperature(58)
            verifyCurrentLocation("Mock Location")
        }
    }
}
