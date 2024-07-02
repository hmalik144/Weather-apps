package com.appttude.h_mal.atlas_weather.robot

import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R

fun furtherInfoScreen(func: FurtherInfoScreen.() -> Unit) = FurtherInfoScreen().apply { func() }
class FurtherInfoScreen : BaseTestRobot() {
    fun verifyMaxTemperature(temperature: Int) =
        matchText(R.id.maxtemp, StringBuilder().append(temperature).append("°").toString())
    fun verifyAverageTemperature(temperature: Int) =
        matchText(R.id.averagetemp, StringBuilder().append(temperature).append("°").toString())
    fun verifyMinTemperature(temperature: Int) =
        matchText(R.id.minimumtemp, StringBuilder().append(temperature).append("°").toString())

    fun verifyWindSpeed(speedText: String) =
        matchText(R.id.windtext, speedText)

    fun verifyHumidity(humidity: Int) =
        matchText(R.id.humiditytext, humidity.toString())
    fun verifyPrecipitation(precipitation: Int) =
        matchText(R.id.preciptext, precipitation.toString())

    fun verifyCloudCoverage(coverage: Int) =
        matchText(R.id.cloudtext, coverage.toString())

    fun verifyUvIndex(uv: Int) =
        matchText(R.id.uvtext, uv.toString())
    fun verifySunrise(sunrise: String) =
        matchText(R.id.sunrisetext, sunrise)
    fun verifySunset(sunset: String) =
        matchText(R.id.sunsettext, sunset)

    fun refresh() = pullToRefresh(R.id.swipe_refresh)
    fun isDisplayed() = matchViewWaitFor(R.id.maxtemp)
}