package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecast.ViewHolderForecast
import com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecastDaily.ViewHolderForecastDaily
import com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.further.ViewHolderFurtherDetails
import com.appttude.h_mal.atlas_weather.utils.generateView

class WeatherRecyclerAdapter(
        private val itemClick: (Forecast) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var weather: WeatherDisplay? = null

    fun addCurrent(current: WeatherDisplay){
        weather = current
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (getDataType(viewType)){
            is ViewType.Empty -> {
                val emptyViewHolder = View(parent.context)
                EmptyViewHolder(emptyViewHolder)
            }
            is ViewType.Current -> {
                val viewCurrent = parent.generateView(R.layout.mono_item_one)
                ViewHolderCurrent(viewCurrent)
            }
            is ViewType.Forecast -> {
                val viewForecast = parent.generateView(R.layout.mono_item_forecast)
                ViewHolderForecast(viewForecast, itemClick)
            }
            is ViewType.Further -> {
                val viewFurther = parent.generateView(R.layout.mono_item_two)
                ViewHolderFurtherDetails(viewFurther)
            }
            is ViewType.ForecastDaily -> {
                val viewForecast = parent.generateView(R.layout.list_item_forecast)
                ViewHolderForecastDaily(viewForecast)
            }
        }
    }

    sealed class ViewType{
        object Empty : ViewType()
        object Current : ViewType()
        object Forecast : ViewType()
        object ForecastDaily : ViewType()
        object Further : ViewType()
    }

    private fun getDataType(type: Int): ViewType {
        return when (type){
            0 -> ViewType.Empty
            1 -> ViewType.Current
            2 -> ViewType.Forecast
            3 -> ViewType.Further
            4 -> ViewType.ForecastDaily
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (weather == null) return 0
        return when(position){
            0 -> 1
            1 -> 3
            2  -> 2
            in 3 until (itemCount - 1) -> 4
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getDataType(getItemViewType(position))){
            is ViewType.Empty -> {
                holder as EmptyViewHolder

            }
            is ViewType.Current -> {
                val viewHolderCurrent = holder as ViewHolderCurrent
                viewHolderCurrent.bindData(weather)
            }
            is ViewType.Further -> {
                val viewHolderCurrent = holder as ViewHolderFurtherDetails
                viewHolderCurrent.bindData(weather)
            }
            is ViewType.Forecast -> {
                val viewHolderForecast = holder as ViewHolderForecast
                viewHolderForecast.bindView(weather?.forecast)
            }
            is ViewType.ForecastDaily -> {
                val viewHolderForecast = holder as ViewHolderForecastDaily
                weather?.forecast?.getOrNull(position -3)?.let {
                    viewHolderForecast.bindView(it)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        if (weather == null) return 0
        return 3 + (weather?.forecast?.size ?: 0)
    }

}