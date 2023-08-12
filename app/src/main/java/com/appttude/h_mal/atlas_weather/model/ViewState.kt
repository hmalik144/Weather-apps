package com.appttude.h_mal.atlas_weather.model

sealed class ViewState {
    object HasStarted : ViewState()
    class HasData<T : Any>(val data: T) : ViewState()
    class HasError<T : Any>(val error: T) : ViewState()
}