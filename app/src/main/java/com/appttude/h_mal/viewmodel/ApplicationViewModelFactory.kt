package com.appttude.h_mal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.data.location.LocationProvider
import com.appttude.h_mal.data.repository.RepositoryImpl


class ApplicationViewModelFactory(
    private val locationProvider: LocationProvider,
    private val repository: RepositoryImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        with(modelClass){
            return when{
                isAssignableFrom(WorldViewModel::class.java) -> WorldViewModel(locationProvider, repository)
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(locationProvider, repository)
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            } as T
        }
    }

}