package com.appttude.h_mal.atlas_weather.monoWeather.testsuite

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.monoWeather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import org.junit.After
import org.junit.Rule

open class BaseTest {

    lateinit var testApp: TestAppClass

    @Rule
    @JvmField
    var mActivityTestRule: ActivityTestRule<MainActivity> = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()

            testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestAppClass
            setupFeed()
        }

        override fun afterActivityLaunched() {

            // Dismiss dialog
            onView(withText("AGREE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(ViewActions.click())
        }
    }

    fun stubEndpoint(url: String, stub: Stubs) {
        testApp.stubUrl(url, stub.id)
    }

    fun unstubEndpoint(url: String) {
        testApp.removeUrlStub(url)
    }

    @After
    fun tearDown() {}

    open fun setupFeed() {}
}