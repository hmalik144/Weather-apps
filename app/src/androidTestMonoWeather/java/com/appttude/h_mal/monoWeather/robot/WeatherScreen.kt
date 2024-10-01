package com.appttude.h_mal.monoWeather.robot

import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.monoWeather.ui.home.adapter.forecastDaily.ViewHolderForecastDaily

fun weatherScreen(func: WeatherScreen.() -> Unit) = WeatherScreen().apply { func() }
class WeatherScreen : BaseTestRobot() {
    fun verifyCurrentTemperature(temperature: Int) =
        matchText(R.id.temp_main_4, temperature.toString())

    fun verifyCurrentLocation(location: String) = matchText(R.id.location_main_4, location)
    fun refresh() = pullToRefresh(R.id.swipe_refresh)
    fun isDisplayed() = matchViewWaitFor(R.id.temp_main_4)

    fun verifyUnableToRetrieve() {
        matchText(R.id.header_text, R.string.retrieve_warning)
        matchText(R.id.body_text, R.string.empty_retrieve_warning)
    }

    fun tapDayInformationByPosition(position: Int) {
        clickSubViewInRecycler<ViewHolderForecastDaily>(R.id.forecast_listview, position)
    }
}