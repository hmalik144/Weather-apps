@file:Suppress("EmptyMethod")

package com.appttude.h_mal.atlas_weather

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.helpers.SnapshotRule
import com.appttude.h_mal.atlas_weather.utils.Stubs
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule

@Suppress("EmptyMethod")
open class BaseTest<A : Activity>(
    private val activity: Class<A>,
    private val intentBundle: Bundle? = null,
) {

    lateinit var scenario: ActivityScenario<A>
    private lateinit var testApp: TestAppClass
    private lateinit var testActivity: Activity
    private lateinit var decorView: View

    private val prefs by lazy { PreferenceProvider(ApplicationProvider.getApplicationContext()) }

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var writePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var snapshotRule: SnapshotRule = SnapshotRule()

    @Rule @JvmField
    val localeTestRule = LocaleTestRule()

    @Before
    fun setUp() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        val startIntent =
            Intent(InstrumentationRegistry.getInstrumentation().targetContext, activity)
        if (intentBundle != null) {
            startIntent.replaceExtras(intentBundle)
        }

        testApp =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        runBlocking {
            beforeLaunch()
        }

        scenario = ActivityScenario.launch(startIntent)
        scenario.onActivity {
            decorView = it.window.decorView
            testActivity = it
        }
        afterLaunch()
    }

    fun stubEndpoint(url: String, stub: Stubs, code: Int = 200) {
        testApp.stubUrl(url, stub.id, code)
    }

    fun unstubEndpoint(url: String) {
        testApp.removeUrlStub(url)
    }

    fun stubLocation(location: String, lat: Double = 0.00, long: Double = 0.00) {
        testApp.stubLocation(location, lat, long)
    }

    fun clearLocation(location: String) {
        testApp.removeLocation(location)
    }

    fun clearLocation(lat: Double, long: Double) {
        testApp.removeLocation(lat, long)
    }

    fun clearPrefs() = prefs.clearPrefs()

    fun clearDatabase() = testApp.clearDatabase()

    fun getActivity() = testActivity

    @After
    fun tearDown() {
        testFinished()
    }

    open fun beforeLaunch() {}
    open fun afterLaunch() {}
    open fun testFinished() {}

    fun waitFor(delay: Long) {
        Espresso.onView(ViewMatchers.isRoot()).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        })
    }

    @Suppress("DEPRECATION")
    fun checkToastMessage(message: String) {
        Espresso.onView(ViewMatchers.withText(message)).inRoot(withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            waitFor(3500)
        }
    }
}