package com.appttude.h_mal.atlas_weather.robot

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.helpers.EspressoHelper.waitForView
import com.appttude.h_mal.atlas_weather.model.types.UnitType
import com.appttude.h_mal.atlas_weather.model.types.UnitType.Companion.getLabel


fun settingsScreen(func: SettingsScreen.() -> Unit) = SettingsScreen().apply { func() }
class SettingsScreen : BaseTestRobot() {

    fun selectWeatherUnits(unitType: UnitType) {
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<ViewHolder>(
                    ViewMatchers.hasDescendant(withText(R.string.weather_units)),
                    click()))
        val label = unitType.getLabel()

        onView(withText(label))
            .inRoot(isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())
    }


    fun verifyCurrentTemperature(temperature: Int) =
        matchText(R.id.temp_main_4, temperature.toString())

    fun verifyCurrentLocation(location: String) = matchText(R.id.location_main_4, location)
    fun refresh() = pullToRefresh(R.id.swipe_refresh)

    fun verifyUnableToRetrieve() {
        matchText(R.id.header_text, R.string.retrieve_warning)
        matchText(R.id.body_text, R.string.empty_retrieve_warning)
    }

    fun isDisplayed() {
        waitForView(
            withText("Metric")
        )
    }
}