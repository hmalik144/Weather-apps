package com.appttude.h_mal.monoWeather.ui.home.adapter.forecast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.weather.Hour
import com.appttude.h_mal.atlas_weather.utils.loadImage
import com.appttude.h_mal.atlas_weather.utils.toTime

class GridCellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dayTV: TextView = itemView.findViewById(R.id.widget_item_day)
    var weatherIV: ImageView = itemView.findViewById(R.id.widget_item_image)
    var mainTempTV: TextView = itemView.findViewById(R.id.widget_item_temp_high)

    fun bindView(hour: Hour?) {
        dayTV.text = hour?.dt?.toTime()
        weatherIV.loadImage(hour?.icon)
        mainTempTV.text = hour?.temp?.toInt()?.toString()
    }
}