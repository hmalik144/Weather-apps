@file:Suppress("EmptyMethod")

package com.appttude.h_mal.atlas_weather

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Root
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.appttude.h_mal.atlas_weather.application.TestAppClass
import com.appttude.h_mal.atlas_weather.helpers.BaseCustomMatcher
import com.appttude.h_mal.atlas_weather.helpers.BaseViewAction
import com.appttude.h_mal.atlas_weather.helpers.SnapshotRule
import com.appttude.h_mal.atlas_weather.utils.Stubs
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

@Suppress("EmptyMethod")
open class BaseTest<A : Activity>(
    private val activity: Class<A>,
    private val intentBundle: Bundle? = null,
) {

    lateinit var scenario: ActivityScenario<A>
    private lateinit var testApp: TestAppClass
    private lateinit var testActivity: Activity

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var snapshotRule: SnapshotRule = SnapshotRule()

    @Before
    fun setUp() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        val startIntent =
            Intent(InstrumentationRegistry.getInstrumentation().targetContext, activity)
        if (intentBundle != null) {
            startIntent.replaceExtras(intentBundle)
        }

        testApp =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestAppClass
        runBlocking {
            beforeLaunch()
        }

        scenario = ActivityScenario.launch(startIntent)
        scenario.onActivity {
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
        Espresso.onView(ViewMatchers.withText(message)).inRoot(object : BaseCustomMatcher<Root>() {
            override fun describe(description: Description?) {
                description?.appendText("is toast")
            }

            override fun matchesSafely(root: Root): Boolean {
                root.run {
                    if (windowLayoutParams.get().type == WindowManager.LayoutParams.TYPE_TOAST) {
                        decorView.run {
                            if (windowToken === applicationWindowToken) {
                                // windowToken == appToken means this window isn't contained by any other windows.
                                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                                return true
                            }
                        }
                    }
                }
                return false
            }
        }
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            waitFor(3500)
        }
    }
}