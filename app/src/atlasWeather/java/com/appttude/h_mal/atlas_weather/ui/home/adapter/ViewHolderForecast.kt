package com.appttude.h_mal.atlas_weather.atlasWeather.ui.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.utils.loadImage

class ViewHolderForecast(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dateTV: TextView = itemView.findViewById(R.id.list_date)
    var dayTV: TextView = itemView.findViewById(R.id.list_day)
    var conditionTV: TextView = itemView.findViewById(R.id.list_condition)
    var weatherIV: ImageView = itemView.findViewById(R.id.list_icon)
    var mainTempTV: TextView = itemView.findViewById(R.id.list_main_temp)
    var minorTempTV: TextView = itemView.findViewById(R.id.list_minor_temp)

    fun bindView(forecast: Forecast?) {
        dateTV.text = forecast?.date
        dayTV.text = forecast?.day
        conditionTV.text = forecast?.condition
        weatherIV.loadImage(forecast?.weatherIcon)
        mainTempTV.text = forecast?.mainTemp
        minorTempTV.text = forecast?.minorTemp
    }
}