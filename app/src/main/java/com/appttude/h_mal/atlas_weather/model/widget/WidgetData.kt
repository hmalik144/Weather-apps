package com.appttude.h_mal.atlas_weather.model.widget

import android.graphics.Bitmap

data class WidgetData(
        val location: String?,
        val icon: String?,
        val currentTemp: String?
)

data class InnerWidgetData(
        val date: String?,
        val icon: Bitmap?,
        val highTemp: String?
)


data class InnerWidgetCellData(
        val date: String?,
        val icon: String?,
        val highTemp: String?
)

data class WidgetWeatherCollection(
        val widgetData: WidgetData,
        val forecast: List<InnerWidgetCellData>
)