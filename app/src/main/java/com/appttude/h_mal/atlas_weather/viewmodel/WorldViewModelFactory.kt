package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.RepositoryImpl

class WorldViewModelFactory(
    private val locationProvider: LocationProvider,
    private val repository: RepositoryImpl
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorldViewModel::class.java)) {
            return (WorldViewModel(locationProvider, repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}