package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.utils.loadImage

class GridCellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dayTV: TextView = itemView.findViewById(R.id.widget_item_day)
    var weatherIV: ImageView = itemView.findViewById(R.id.widget_item_image)
    var mainTempTV: TextView = itemView.findViewById(R.id.widget_item_temp_high)
    var lowTempTV: TextView = itemView.findViewById(R.id.widget_item_temp_low)

    fun bindView(forecast: Forecast?) {
        dayTV.text = forecast?.date
        weatherIV.loadImage(forecast?.weatherIcon, 64, 64)
        mainTempTV.text = forecast?.mainTemp
        lowTempTV.text = forecast?.minorTemp
    }
}