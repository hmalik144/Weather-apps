package com.appttude.h_mal.atlas_weather.data.network

class NetworkModule : BaseNetworkModule() {
    override fun baseUrl(): String = "https://api.openweathermap.org/data/2.5/"
}

