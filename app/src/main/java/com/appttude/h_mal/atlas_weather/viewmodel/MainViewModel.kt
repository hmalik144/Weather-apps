package com.appttude.h_mal.atlas_weather.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.model.weather.Current
import com.appttude.h_mal.atlas_weather.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    private val locationProvider: LocationProvider,
    private val repository: Repository
): ViewModel(){

    val currentWeatherLiveData = MutableLiveData<Current>()
    val weatherLiveData = MutableLiveData<WeatherDisplay>()

    val operationState = MutableLiveData<Event<Boolean>>()
    val operationError = MutableLiveData<Event<String>>()

    init {
        repository.loadCurrentWeatherFromRoom(CURRENT_LOCATION).observeForever {
            it?.weather?.let { data ->
                val list= WeatherDisplay(data).apply {
                    unit = "°C"
                    location = locationProvider.getLocationName(data.lat, data.lon)
                }

                weatherLiveData.postValue(list)
            }

        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchData(){
        if (!repository.isSearchValid(CURRENT_LOCATION)) return
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                // Get location
                val latLong = locationProvider.getLatLong()
                // Get weather from api
                val weather = repository
                        .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())

                // Save data if not null
                weather.let {
                    repository.saveLastSavedAt(CURRENT_LOCATION)
                    repository.saveCurrentWeatherToRoom(CURRENT_LOCATION, it)
                    return@launch
                }
            }catch (e: IOException){
                operationError.postValue(Event(e.message!!))
            }finally {
                operationState.postValue(Event(false))
            }
        }
    }
}