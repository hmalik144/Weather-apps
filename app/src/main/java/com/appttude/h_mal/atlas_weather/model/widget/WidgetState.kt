package com.appttude.h_mal.atlas_weather.model.widget

sealed class WidgetState {
    class HasData<T : Any>(val data: T) : WidgetState()
    class HasError<T : Any>(val error: T) : WidgetState()
}