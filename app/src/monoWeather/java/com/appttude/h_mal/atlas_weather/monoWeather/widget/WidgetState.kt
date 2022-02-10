package com.appttude.h_mal.atlas_weather.monoWeather.widget

enum class WidgetState {
    NO_LOCATION,
    SCREEN_ON_CONNECTION_AVAILABLE,
    SCREEN_ON_CONNECTION_UNAVAILABLE,
    SCREEN_OFF_CONNECTION_AVAILABLE,
    SCREEN_OFF_CONNECTION_UNAVAILABLE;

    companion object {
        fun getWidgetState(
                locationAvailable: Boolean,
                screenOn: Boolean,
                connectionAvailable: Boolean
        ): WidgetState {
            return if (!locationAvailable)
                NO_LOCATION
            else if (screenOn && connectionAvailable)
                SCREEN_ON_CONNECTION_AVAILABLE
            else if (screenOn && !connectionAvailable)
                SCREEN_ON_CONNECTION_UNAVAILABLE
            else if (!screenOn && connectionAvailable)
                SCREEN_OFF_CONNECTION_AVAILABLE
            else
                SCREEN_OFF_CONNECTION_UNAVAILABLE
        }
    }
}