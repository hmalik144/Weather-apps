package com.appttude.h_mal.atlas_weather.snapshot


import android.annotation.TargetApi
import androidx.test.filters.SmallTest
import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.monoWeather.robot.furtherInfoScreen
import com.appttude.h_mal.monoWeather.robot.settingsScreen
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import org.junit.Test
import tools.fastlane.screengrab.Screengrab

@SmallTest
@TargetApi(27)
class SnapshotCaptureTest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Metric)
        stubLocation("London", 51.51, -0.13)
        clearPrefs()
    }


    @Test
    fun homeAndFurtherInfoPageCapture() {
        weatherScreen {
            isDisplayed()
            Screengrab.screenshot("HomeScreen")
            tapDayInformationByPosition(4)
        }
        furtherInfoScreen {
            isDisplayed()
            Screengrab.screenshot("FurtherInfoScreen")
        }

    }

    @Test
    fun settingsPageCapture() {
        weatherScreen {
            isDisplayed()
            openMenuItem()
        }
        settingsScreen {
            stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Imperial)
            Screengrab.screenshot("SettingsScreen")
        }
    }
}
