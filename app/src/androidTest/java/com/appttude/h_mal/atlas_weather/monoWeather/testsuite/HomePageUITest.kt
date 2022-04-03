package com.appttude.h_mal.atlas_weather.monoWeather.testsuite


import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.monoWeather.robot.homeScreen
import com.appttude.h_mal.atlas_weather.utils.Stubs
import org.junit.Rule
import org.junit.Test

class HomePageUITest : BaseTest() {

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_COARSE_LOCATION")

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
