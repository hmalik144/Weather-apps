package com.appttude.h_mal.atlas_weather.ui.home.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay

internal class ViewHolderFurtherDetails(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var windSpeed: TextView = itemView.findViewById(R.id.windspeed)
    var windDirection: TextView = itemView.findViewById(R.id.winddirection)
    var precipitation: TextView = itemView.findViewById(R.id.precip_)
    var humidity: TextView = itemView.findViewById(R.id.humidity_)
    var clouds: TextView = itemView.findViewById(R.id.clouds_)

    fun bindData(weather: WeatherDisplay?){
        windSpeed.text = weather?.windSpeed
        windDirection.text = weather?.windDirection
        precipitation.text = weather?.precipitation
        humidity.text = weather?.humidity
        clouds.text = weather?.clouds
    }
}