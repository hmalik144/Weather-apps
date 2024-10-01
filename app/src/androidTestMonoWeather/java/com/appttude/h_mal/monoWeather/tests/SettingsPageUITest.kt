package com.appttude.h_mal.monoWeather.tests


import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import com.appttude.h_mal.monoWeather.robot.settingsScreen
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import org.junit.Test

class SettingsPageUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.Metric)
        clearPrefs()
    }

    @Test
    fun loadApp_changeToImperial_returnsValidPage() {
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(13)
            verifyCurrentLocation("Mock Location")
            stubEndpoint(baseUrl, Stubs.Imperial)
            openMenuItem()
        }
        settingsScreen {
            selectWeatherUnits(UnitType.IMPERIAL)
            goBack()
        }
        weatherScreen {
            isDisplayed()
            refresh()
            verifyCurrentTemperature(56)
            verifyCurrentLocation("Mock Location")
        }
    }
}
