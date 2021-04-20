package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.weather.Hour
import com.appttude.h_mal.atlas_weather.utils.generateView

class GridForecastAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var weather: MutableList<Hour> = mutableListOf()

    fun addCurrent(current: List<Hour>?){
        weather.clear()
        current?.let { weather.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewCurrent = parent.generateView(R.layout.mono_forecast_grid_item)
        return GridCellHolder(viewCurrent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as GridCellHolder
        val forecast = weather[position]
        view.bindView(forecast)
    }

    override fun getItemCount(): Int = weather.size

}