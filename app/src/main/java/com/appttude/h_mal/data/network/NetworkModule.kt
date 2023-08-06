package com.appttude.h_mal.data.network

class NetworkModule : BaseNetworkModule() {
    override fun baseUrl(): String = "https://api.openweathermap.org/data/2.5/"
}

