package com.appttude.h_mal.atlas_weather

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.helper.GenericsHelper.getGenericClassAt
import com.appttude.h_mal.atlas_weather.helpers.SnapshotRule
import com.appttude.h_mal.atlas_weather.utils.Stubs
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

open class BaseTest<A : Activity>(
    private val activity: Class<A>,
    private val intentBundle: Bundle? = null,
) {

    lateinit var scenario: ActivityScenario<A>
    private lateinit var testApp: TestAppClass

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var snapshotRule: SnapshotRule = SnapshotRule()

    @Before
    fun setUp() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        val startIntent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, activity)
        if (intentBundle != null) {
            startIntent.replaceExtras(intentBundle)
        }

        scenario = ActivityScenario.launch(startIntent)
        scenario.onActivity {
            runBlocking {
                testApp = it.application as TestAppClass
                beforeLaunch()
            }
        }
        afterLaunch()
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

    open fun beforeLaunch() {}
    open fun afterLaunch() {}
    open fun testFinished() {}
}