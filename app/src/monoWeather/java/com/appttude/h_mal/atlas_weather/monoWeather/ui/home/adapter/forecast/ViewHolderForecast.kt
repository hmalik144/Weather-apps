package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.utils.loadImage

class ViewHolderForecast(
        itemView: View,
        private val itemClick: (Forecast) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    var recyclerView: RecyclerView = itemView.findViewById(R.id.forecast_recyclerview)

    fun bindView(forecasts: List<Forecast>?) {
        val adapter = GridForecastAdapter(itemClick)
        adapter.addCurrent(forecasts)
        recyclerView.adapter = adapter

    }
}