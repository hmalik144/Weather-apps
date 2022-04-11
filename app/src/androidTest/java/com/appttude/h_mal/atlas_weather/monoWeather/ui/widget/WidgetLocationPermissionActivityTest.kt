package com.appttude.h_mal.atlas_weather.monoWeather.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import com.appttude.h_mal.atlas_weather.R
import org.junit.Rule
import org.junit.Test


class WidgetLocationPermissionActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule : ActivityTestRule<WidgetLocationPermissionActivity> =
            ActivityTestRule<WidgetLocationPermissionActivity>(WidgetLocationPermissionActivity::class.java, false, false)

    @Test
    fun demo_test() {
        val i = Intent()
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 112)
        mActivityTestRule.launchActivity(i)

        Espresso.onView((ViewMatchers.withId(R.id.declaration_text))).check(matches(isDisplayed()));
    }
}