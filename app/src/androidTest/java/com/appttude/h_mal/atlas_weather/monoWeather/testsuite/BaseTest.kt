package com.appttude.h_mal.atlas_weather.testsuite

import android.Manifest
import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.helper.GenericsHelper.getGenericClassAt
import com.appttude.h_mal.atlas_weather.helpers.SnapshotRule
import com.appttude.h_mal.atlas_weather.utils.Stubs
import org.junit.After
import org.junit.Rule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

open class BaseTest<A : Activity> {

    lateinit var testApp: TestAppClass

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var snapshotRule: SnapshotRule = SnapshotRule()

    @Rule
    @JvmField
    var mActivityTestRule: ActivityTestRule<A> =
        object : ActivityTestRule<A>(getGenericClassAt<A>(0).java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

                testApp =
                    InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestAppClass
                setupFeed()
            }

            override fun afterActivityLaunched() {

                // Dismiss dialog
                onView(withText("AGREE")).inRoot(isDialog()).check(matches(isDisplayed()))
                    .perform(ViewActions.click())
            }
        }

    fun stubEndpoint(url: String, stub: Stubs) {
        testApp.stubUrl(url, stub.id)
    }

    fun unstubEndpoint(url: String) {
        testApp.removeUrlStub(url)
    }

    @After
    fun tearDown() {
    }

    open fun setupFeed() {}
}