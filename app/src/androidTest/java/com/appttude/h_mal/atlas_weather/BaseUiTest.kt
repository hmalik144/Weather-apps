//package com.appttude.h_mal.atlas_weather
//
//import android.Manifest
//import android.R
//import android.app.Activity
//import android.content.Context
//import android.os.Build
//import android.view.View
//import android.view.WindowManager
//import androidx.annotation.StringRes
//import androidx.test.core.app.ActivityScenario
//import androidx.test.espresso.*
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
//import androidx.test.rule.GrantPermissionRule
//import com.appttude.h_mal.atlas_weather.atlasWeather.ui.BaseActivity
////import h_mal.appttude.com.driver.base.BaseActivity
//import com.appttude.h_mal.atlas_weather.helpers.BaseViewAction
//import com.appttude.h_mal.atlas_weather.helpers.SnapshotRule
//import org.hamcrest.CoreMatchers
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//import org.hamcrest.TypeSafeMatcher
//import org.hamcrest.core.AllOf
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import tools.fastlane.screengrab.Screengrab
//import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
//import tools.fastlane.screengrab.locale.LocaleTestRule
//
//
//open class BaseUiTest<T : BaseActivity>(
//    private val activity: Class<T>
//) {
//
//    private lateinit var mActivityScenarioRule: ActivityScenario<T>
//    private var mIdlingResource: IdlingResource? = null
//
//    private lateinit var currentActivity: Activity
//
//    @get:Rule
//    var permissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//    @get:Rule
//    var snapshotRule: SnapshotRule = SnapshotRule()
//
//    @Rule
//    @JvmField
//    var localeTestRule = LocaleTestRule()
//
//    @Before
//    fun setup() {
//        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
//        beforeLaunch()
//        mActivityScenarioRule = ActivityScenario.launch(activity)
//        mActivityScenarioRule.onActivity {
////            mIdlingResource = it.getIdlingResource()!!
////            IdlingRegistry.getInstance().register(mIdlingResource)
//            afterLaunch(it)
//        }
//    }
//
//    @After
//    fun tearDown() {
//        mIdlingResource?.let {
//            IdlingRegistry.getInstance().unregister(it)
//        }
//    }
//
//    fun getResourceString(@StringRes stringRes: Int): String {
//        return getInstrumentation().targetContext.resources.getString(
//            stringRes
//        )
//    }
//
//    fun waitFor(delay: Long) {
//        onView(isRoot()).perform(object : ViewAction {
//            override fun getConstraints(): Matcher<View> = isRoot()
//            override fun getDescription(): String = "wait for $delay milliseconds"
//            override fun perform(uiController: UiController, v: View?) {
//                uiController.loopMainThreadForAtLeast(delay)
//            }
//        })
//    }
//
//    open fun beforeLaunch() {}
//    open fun afterLaunch(context: Context) {}
//
//
//    @Suppress("DEPRECATION")
//    fun checkToastMessage(message: String) {
//        onView(withText(message)).inRoot(object : TypeSafeMatcher<Root>() {
//            override fun describeTo(description: Description?) {
//                description?.appendText("is toast")
//            }
//
//            override fun matchesSafely(root: Root): Boolean {
//                root.run {
//                    if (windowLayoutParams.get().type == WindowManager.LayoutParams.TYPE_TOAST) {
//                        decorView.run {
//                            if (windowToken === applicationWindowToken) {
//                                // windowToken == appToken means this window isn't contained by any other windows.
//                                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
//                                return true
//                            }
//                        }
//                    }
//                }
//                return false
//            }
//        }
//        ).check(matches(isDisplayed()))
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            waitFor(3500)
//        }
//    }
//
//    fun checkSnackBarDisplayedByMessage(message: String) {
//        onView(
//            CoreMatchers.allOf(
//                withId(com.google.android.material.R.id.snackbar_text),
//                withText(message)
//            )
//        ).check(matches(isDisplayed()))
//    }
//
//    private fun getCurrentActivity(): Activity {
//        onView(AllOf.allOf(withId(R.id.content), isDisplayed()))
//            .perform(object : BaseViewAction() {
//                override fun setPerform(uiController: UiController?, view: View?) {
//                    if (view?.context is Activity) {
//                        currentActivity = view.context as Activity
//                    }
//                }
//            })
//        return currentActivity
//    }
//}