package com.appttude.h_mal.atlas_weather.model

interface DataMapper <T: Any> {
    fun mapData(): T
}