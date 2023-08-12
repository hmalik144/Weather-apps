package com.appttude.h_mal.monoWeather.robot

import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R

fun addLocation(func: AddLocationScreenRobot.() -> Unit) = AddLocationScreenRobot().apply { func() }
class AddLocationScreenRobot : BaseTestRobot() {
    fun setLocation(location: String) =
        fillEditText(R.id.location_name_tv, location)

    fun submit() = clickButton(R.id.submit)
}