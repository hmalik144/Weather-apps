package com.appttude.h_mal.monoWeather.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.generateView
import com.appttude.h_mal.monoWeather.ui.EmptyViewHolder
import com.appttude.h_mal.monoWeather.ui.home.adapter.forecast.ViewHolderForecast
import com.appttude.h_mal.monoWeather.ui.home.adapter.forecastDaily.ViewHolderForecastDaily
import com.appttude.h_mal.monoWeather.ui.home.adapter.further.ViewHolderFurtherDetails

class WeatherRecyclerAdapter(
    private val itemClick: (Forecast) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var weather: WeatherDisplay? = null

    fun addCurrent(current: WeatherDisplay) {
        weather = current
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (getDataType(viewType)) {
            is ViewType.Empty -> {
                val emptyViewHolder = parent.generateView(R.layout.empty_state_layout)
                EmptyViewHolder(emptyViewHolder)
            }

            is ViewType.Current -> {
                val viewCurrent = parent.generateView(R.layout.mono_item_one)
                ViewHolderCurrent(viewCurrent)
            }

            is ViewType.ForecastHourly -> {
                val viewForecast = parent.generateView(R.layout.mono_item_forecast)
                ViewHolderForecast(viewForecast)
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

    sealed class ViewType {
        object Empty : ViewType()
        object Current : ViewType()
        object ForecastHourly : ViewType()
        object ForecastDaily : ViewType()
        object Further : ViewType()
    }

    private fun getDataType(type: Int): ViewType {
        return when (type) {
            0 -> ViewType.Empty
            1 -> ViewType.Current
            2 -> ViewType.ForecastHourly
            3 -> ViewType.Further
            4 -> ViewType.ForecastDaily
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (weather == null) return 0
        return when (position) {
            0 -> 1
            1 -> 3
            2 -> 2
            in 3 until (itemCount) -> 4
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getDataType(getItemViewType(position))) {
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

            is ViewType.ForecastHourly -> {
                val viewHolderForecast = holder as ViewHolderForecast
                viewHolderForecast.bindView(weather?.hourly)
            }

            is ViewType.ForecastDaily -> {
                val viewHolderForecast = holder as ViewHolderForecastDaily
                weather?.forecast?.getOrNull(position - 3)?.let { f ->
                    viewHolderForecast.bindView(f)
                    viewHolderForecast.itemView.setOnClickListener {
                        itemClick.invoke(f)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (weather == null) 0 else 3 + (weather?.forecast?.size ?: 0)
    }

}