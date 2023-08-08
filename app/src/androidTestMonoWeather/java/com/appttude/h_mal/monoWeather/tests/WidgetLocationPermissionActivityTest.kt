package com.appttude.h_mal.monoWeather.tests

import android.appwidget.AppWidgetManager
import android.os.Bundle
import com.appttude.h_mal.atlas_weather.BaseTest
import com.appttude.h_mal.monoWeather.robot.widgetPermissionScreen
import com.appttude.h_mal.monoWeather.ui.widget.WidgetLocationPermissionActivity
import org.junit.Test


class WidgetLocationPermissionActivityTest : BaseTest<WidgetLocationPermissionActivity>(
    activity = WidgetLocationPermissionActivity::class.java,
    intentBundle = Bundle().apply {
        putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 112)
    }
) {

    @Test
    fun demo_test() {
        widgetPermissionScreen {
            declarationDisplayed()
        }
    }
}