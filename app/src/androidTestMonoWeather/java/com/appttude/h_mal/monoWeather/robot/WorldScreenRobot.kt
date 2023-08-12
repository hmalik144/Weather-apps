package com.appttude.h_mal.monoWeather.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.helpers.EspressoHelper

fun world(func: WorldScreenRobot.() -> Unit) = WorldScreenRobot().apply { func() }
class WorldScreenRobot : BaseTestRobot() {
    fun clickFab() = clickButton(R.id.floatingActionButton)
    fun clickItemInList(location: String) {
        EspressoHelper.waitForView(withId(R.id.world_recycler))
        clickViewInRecycler<RecyclerView.ViewHolder>(R.id.world_recycler, location)
    }

    fun clickItemInListByPosition(position: Int) =
        clickViewInRecycler<RecyclerView.ViewHolder>(R.id.world_recycler, position)

    fun emptyViewDisplayed() {
        matchText(R.id.body_text, R.string.retrieve_warning)
        matchText(R.id.header_text, R.string.empty_retrieve_warning)
    }
}