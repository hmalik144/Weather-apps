package com.appttude.h_mal.atlas_weather.monoWeather.testsuite

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