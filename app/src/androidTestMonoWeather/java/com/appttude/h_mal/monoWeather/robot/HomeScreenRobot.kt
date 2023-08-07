package com.appttude.h_mal.monoWeather.robot

import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.BaseTestRobot

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }
class HomeScreenRobot : BaseTestRobot() {
    fun verifyCurrentTemperature(temperature: Int) = matchText(R.id.temp_main_4, temperature.toString())
    fun verifyCurrentLocation(location: String) = matchText(R.id.location_main_4, location)
    fun refresh() = pullToRefresh(R.id.swipe_refresh)
}