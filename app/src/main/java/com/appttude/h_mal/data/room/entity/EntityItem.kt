package com.appttude.h_mal.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appttude.h_mal.model.weather.FullWeather


const val CURRENT_LOCATION = "CurrentLocation"
@Entity
data class EntityItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val weather: FullWeather
)