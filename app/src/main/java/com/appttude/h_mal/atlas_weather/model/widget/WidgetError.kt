package com.appttude.h_mal.atlas_weather.model.widget

import androidx.annotation.DrawableRes

data class WidgetError(
    @DrawableRes
    val icon: Int,
    val errorMessage: String
)
