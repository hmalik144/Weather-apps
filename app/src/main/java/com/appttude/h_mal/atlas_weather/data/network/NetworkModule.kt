package com.appttude.h_mal.atlas_weather.data.network

class NetworkModule : BaseNetworkModule() {
    override fun baseUrl(): String = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
}

