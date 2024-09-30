package com.appttude.h_mal.monoWeather.tests


import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.Stubs
import com.appttude.h_mal.atlas_weather.utils.baseUrl
import com.appttude.h_mal.monoWeather.robot.ContainerRobot.Tab.WORLD
import com.appttude.h_mal.monoWeather.robot.addLocation
import com.appttude.h_mal.monoWeather.robot.container
import com.appttude.h_mal.monoWeather.robot.weatherScreen
import com.appttude.h_mal.monoWeather.robot.world
import org.junit.Test

class WorldPageUITest : BaseTest<MainActivity>(MainActivity::class.java) {

    override fun beforeLaunch() {
        stubEndpoint(baseUrl, Stubs.New)
        stubLocation("London", 51.5064, -0.12721)
    }

    @Test
    fun loadApp_addNewLocation_returnsValidPage() {
        container {
            tapTabInBottomBar(WORLD)
        }
        world {
            clickFab()
        }
        addLocation {
            // Todo: change this
            stubEndpoint("https://api.openweathermap.org/data/2.5/onecall", Stubs.Sydney)
            stubLocation("Sydney", -33.89, -151.12)
            setLocation("Sydney")
            submit()
        }
        world {
            clickItemInList("Sydney")
        }
        weatherScreen {
            isDisplayed()
            verifyCurrentTemperature(12)
            verifyCurrentLocation("Sydney")
        }
    }
}
