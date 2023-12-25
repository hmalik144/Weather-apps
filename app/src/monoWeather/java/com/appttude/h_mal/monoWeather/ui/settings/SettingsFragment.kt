package com.appttude.h_mal.monoWeather.ui.settings

import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.base.BasePreferencesFragment
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.viewmodel.SettingsViewModel

class SettingsFragment : BasePreferencesFragment<SettingsViewModel>(R.xml.prefs_screen) {

    override fun preferenceChanged(key: String) {
        when (key) {
            "UnitType" -> viewModel.refreshWeatherData()
            "widget_black_background" -> {
                viewModel.updateWidget()
                displayToast("Widget background has been updates")
            }
        }
    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        if (data is String) displayToast(data)
    }
}