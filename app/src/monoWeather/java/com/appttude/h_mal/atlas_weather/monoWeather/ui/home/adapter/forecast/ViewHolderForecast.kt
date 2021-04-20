package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecast

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.weather.Hour

class ViewHolderForecast(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    var recyclerView: RecyclerView = itemView.findViewById(R.id.forecast_recyclerview)

    fun bindView(forecasts: List<Hour>?) {
        val adapter = GridForecastAdapter()
        adapter.addCurrent(forecasts)
        recyclerView.adapter = adapter

    }
}