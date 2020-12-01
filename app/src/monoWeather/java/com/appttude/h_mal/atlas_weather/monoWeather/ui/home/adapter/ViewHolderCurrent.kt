package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.loadImage

class ViewHolderCurrent(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

    var locationTV: TextView = listItemView.findViewById(R.id.location_main_4)
    var conditionTV: TextView = listItemView.findViewById(R.id.condition_main_4)
    var weatherIV: ImageView = listItemView.findViewById(R.id.icon_main_4)
    var avgTempTV: TextView = listItemView.findViewById(R.id.temp_main_4)
    var tempUnit: TextView = listItemView.findViewById(R.id.temp_unit_4)

    fun bindData(weather: WeatherDisplay?){
        locationTV.text = weather?.location
        conditionTV.text = weather?.description
        weatherIV.loadImage(weather?.iconURL, 64, 64)
        avgTempTV.text = weather?.averageTemp?.toInt().toString()
        tempUnit.text = weather?.unit
    }
}