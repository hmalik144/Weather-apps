package com.appttude.h_mal.atlas_weather.ui.home.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.generateView

class WeatherRecyclerAdapter(
    val itemClick: (Forecast) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var weather: WeatherDisplay? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addCurrent(current: WeatherDisplay) {
        weather = current
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (getDataType(viewType)) {
            is ViewType.Empty -> {
                val emptyViewHolder = View(parent.context)
                EmptyViewHolder(emptyViewHolder)
            }

            is ViewType.Current -> {
                val viewCurrent = parent.generateView(R.layout.list_item_current)
                ViewHolderCurrent(viewCurrent)
            }

            is ViewType.Forecast -> {
                val viewForecast = parent.generateView(R.layout.list_item_forecast)
                ViewHolderForecast(viewForecast)
            }

            is ViewType.Further -> {
                val viewFurther = parent.generateView(R.layout.list_item_further)
                ViewHolderFurtherDetails(viewFurther)
            }
        }
    }

    sealed class ViewType {
        object Empty : ViewType()
        object Current : ViewType()
        object Forecast : ViewType()
        object Further : ViewType()
    }

    private fun getDataType(type: Int): ViewType {
        return when (type) {
            0 -> ViewType.Empty
            1 -> ViewType.Current
            2 -> ViewType.Forecast
            3 -> ViewType.Further
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (weather == null) return 0

        return when (position) {
            0 -> 1
            in 1 until itemCount - 2 -> 2
            itemCount - 1 -> 3
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

            is ViewType.Forecast -> {
                val viewHolderForecast = holder as ViewHolderForecast

                weather?.forecast?.get(position - 1)?.let { i ->
                    viewHolderForecast.bindView(i)
                    viewHolderForecast.itemView.setOnClickListener {
                        itemClick(i)
                    }
                }
            }

            is ViewType.Further -> {
                val viewHolderCurrent = holder as ViewHolderFurtherDetails
                viewHolderCurrent.bindData(weather)
            }
        }

    }

    override fun getItemCount(): Int {
        if (weather == null) return 0
        return 2 + (weather?.forecast?.size ?: 0)
    }

}