package com.appttude.h_mal.atlas_weather.testsuite


import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.robot.homeScreen
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomePageUITestScenario : BaseMainScenario() {

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

open class BaseMainScenario {

    lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var testApp: TestAppClass

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @Before
    fun setUp() {
        scenario = launch(MainActivity::class.java)
        scenario.onActivity {
            runBlocking {
                testApp = it.application as TestAppClass
                setupFeed()
            }
        }

        // Dismiss dialog on start up
        Espresso.onView(ViewMatchers.withText("AGREE"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    fun stubEndpoint(url: String, stub: Stubs) {
        testApp.stubUrl(url, stub.id)
    }

    fun unstubEndpoint(url: String) {
        testApp.removeUrlStub(url)
    }

    @After
    fun tearDown() {
        testFinished()
    }

    open fun setupFeed() {}
    open fun testFinished() {}
}
