package com.appttude.h_mal.monoWeather.robot

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R

fun container(func: ContainerRobot.() -> Unit) = ContainerRobot().apply { func() }
class ContainerRobot : BaseTestRobot() {

    fun tapTabInBottomBar(tab: Tab) {
        when (tab) {
            Tab.HOME -> Espresso.onView(withId(R.id.nav_world))
            Tab.WORLD -> Espresso.onView(withId(R.id.nav_home))
        }.perform(click())
    }

    enum class Tab {
        HOME,
        WORLD
    }
}