package com.appttude.h_mal.atlas_weather.application

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.atlas_weather.data.WeatherSource
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepository
import com.appttude.h_mal.atlas_weather.viewmodel.MainViewModel
import com.appttude.h_mal.atlas_weather.viewmodel.SettingsViewModel
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel


class ApplicationViewModelFactory(
    private val application: Application,
    private val locationProvider: LocationProvider,
    private val source: WeatherSource,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        with(modelClass) {
            return when {
                isAssignableFrom(WorldViewModel::class.java) -> WorldViewModel(
                    locationProvider,
                    source
                )

                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                    locationProvider,
                    source
                )

                isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                    application, locationProvider, source, settingsRepository
                )

                else -> throw IllegalArgumentException("Unknown ViewModel class")
            } as T
        }
    }

}