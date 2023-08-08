package com.appttude.h_mal.monoWeather

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity

open class MonoBaseTest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun afterLaunch() {
        // Dismiss dialog on start up
        Espresso.onView(ViewMatchers.withText("AGREE"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }
}