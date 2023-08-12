package com.appttude.h_mal.atlas_weather.ui.home.adapter.forecastDaily

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.utils.loadImage

class ViewHolderForecastDaily(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dateTV: TextView = itemView.findViewById(R.id.list_date)
    var dayTV: TextView = itemView.findViewById(R.id.list_day)
    var weatherIV: ImageView = itemView.findViewById(R.id.list_icon)
    var maxTempTV: TextView = itemView.findViewById(R.id.list_main_temp)
    var minTempTV: TextView = itemView.findViewById(R.id.list_minor_temp)
    var conditionTV: TextView = itemView.findViewById(R.id.list_condition)

    fun bindView(forecast: Forecast?) {
        dateTV.text = forecast?.date
        dayTV.text = forecast?.day
        weatherIV.loadImage(forecast?.weatherIcon)
        maxTempTV.text = forecast?.mainTemp
        minTempTV.text = forecast?.minorTemp
        conditionTV.text = forecast?.condition
    }
}