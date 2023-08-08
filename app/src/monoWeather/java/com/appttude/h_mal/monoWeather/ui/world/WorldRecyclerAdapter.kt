package com.appttude.h_mal.monoWeather.ui.world

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.generateView
import com.appttude.h_mal.atlas_weather.utils.loadImage
import com.appttude.h_mal.monoWeather.ui.EmptyViewHolder

class WorldRecyclerAdapter(
    private val itemClick: (WeatherDisplay) -> Unit,
    private val itemLongClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var weather: MutableList<WeatherDisplay> = mutableListOf()

    fun addCurrent(current: List<WeatherDisplay>) {
        weather.clear()
        weather.addAll(current)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (getDataType(viewType)) {
            is ViewType.Empty -> {
                val emptyViewHolder = parent.generateView(R.layout.empty_state_layout)
                EmptyViewHolder(emptyViewHolder)
            }

            is ViewType.Current -> {
                val viewCurrent = parent.generateView(R.layout.db_list_item)
                WorldHolderCurrent(viewCurrent)
            }
        }
    }

    sealed class ViewType {
        object Empty : ViewType()
        object Current : ViewType()
    }

    private fun getDataType(type: Int): ViewType {
        return when (type) {
            0 -> ViewType.Empty
            1 -> ViewType.Current
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (weather.isEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getDataType(getItemViewType(position))) {
            is ViewType.Empty -> {
                holder as EmptyViewHolder
                holder.bindData(null, "World List Empty", "Please add a location")
            }

            is ViewType.Current -> {
                val viewHolderCurrent = holder as WorldHolderCurrent
                val currentWeather = weather[position]
                viewHolderCurrent.bindData(currentWeather)
                viewHolderCurrent.itemView.setOnClickListener {
                    itemClick.invoke(weather[position])
                }
                viewHolderCurrent.itemView.setOnLongClickListener {
                    currentWeather.location?.let { location -> itemLongClick.invoke(location) }
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (weather.size == 0) 1 else weather.size
    }

    internal class WorldHolderCurrent(cellView: View) : BaseViewHolder<WeatherDisplay>(cellView) {

        private val locationTV: TextView = cellView.findViewById(R.id.db_location)
        private val conditionTV: TextView = cellView.findViewById(R.id.db_condition)
        private val weatherIV: ImageView = cellView.findViewById(R.id.db_icon)
        private val avgTempTV: TextView = cellView.findViewById(R.id.db_main_temp)
        private val tempUnit: TextView = cellView.findViewById(R.id.db_temp_unit)

        override fun bindData(data: WeatherDisplay?) {
            locationTV.text = data?.displayName
            conditionTV.text = data?.description
            weatherIV.loadImage(data?.iconURL)
            avgTempTV.text = data?.forecast?.get(0)?.mainTemp
            tempUnit.text = itemView.context.getString(R.string.degrees)
        }
    }

    abstract class BaseViewHolder<T : Any>(cellView: View) : RecyclerView.ViewHolder(cellView) {
        abstract fun bindData(data: T?)
    }
}