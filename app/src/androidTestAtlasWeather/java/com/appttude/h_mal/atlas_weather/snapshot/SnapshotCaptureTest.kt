package com.appttude.h_mal.atlas_weather.snapshot


import android.annotation.TargetApi
import androidx.test.filters.SmallTest
import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.robot.furtherInfoScreen
import com.appttude.h_mal.atlas_weather.robot.settingsScreen
import com.appttude.h_mal.atlas_weather.robot.weatherScreen
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import org.junit.Test
import tools.fastlane.screengrab.Screengrab

@SmallTest
@TargetApi(27)
class SnapshotCaptureTest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.Metric)
        stubLocation("London", 51.51, -0.13)
        clearPrefs()
    }

    override fun testFinished() {
        super.testFinished()
        clearLocation("London")
        clearDatabase()
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
            stubEndpoint(baseUrl, Stubs.Imperial)
            Screengrab.screenshot("SettingsScreen")
        }
    }
}
