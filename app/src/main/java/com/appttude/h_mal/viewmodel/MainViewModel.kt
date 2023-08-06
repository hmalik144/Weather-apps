package com.appttude.h_mal.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import com.appttude.h_mal.data.location.LocationProvider
import com.appttude.h_mal.data.repository.Repository
import com.appttude.h_mal.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.data.room.entity.EntityItem
import com.appttude.h_mal.model.forecast.WeatherDisplay
import com.appttude.h_mal.utils.Event
import com.appttude.h_mal.viewmodel.baseViewModels.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
        private val locationProvider: LocationProvider,
        private val repository: Repository
): WeatherViewModel(){

    val weatherLiveData = MutableLiveData<WeatherDisplay>()

    val operationState = MutableLiveData<Event<Boolean>>()
    val operationError = MutableLiveData<Event<String>>()
    val operationRefresh = MutableLiveData<Event<Boolean>>()

    init {
        repository.loadCurrentWeatherFromRoom(CURRENT_LOCATION).observeForever {
            it?.let {
                val weather = WeatherDisplay(it)
                weatherLiveData.postValue(weather)
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    fun fetchData(){
        if (!repository.isSearchValid(CURRENT_LOCATION)){
            operationRefresh.postValue(Event(false))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                // Get location
                val latLong = locationProvider.getCurrentLatLong()
                // Get weather from api
                val weather = repository
                        .getWeatherFromApi(latLong.first.toString(), latLong.second.toString())
                val currentLocation = locationProvider.getLocationNameFromLatLong(weather.lat, weather.lon)
                val fullWeather = createFullWeather(weather, currentLocation)
                val entityItem = EntityItem(CURRENT_LOCATION, fullWeather)
                // Save data if not null
                repository.saveLastSavedAt(CURRENT_LOCATION)
                repository.saveCurrentWeatherToRoom(entityItem)
            }catch (e: Exception){
                operationError.postValue(Event(e.message!!))
            }finally {
                operationState.postValue(Event(false))
                operationRefresh.postValue(Event(false))
            }
        }
    }
}