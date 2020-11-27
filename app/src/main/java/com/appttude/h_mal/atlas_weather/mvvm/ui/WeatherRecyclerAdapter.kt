package com.appttude.h_mal.atlas_weather.mvvm.ui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.mvvm.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.mvvm.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.mvvm.utils.generateView
import com.appttude.h_mal.atlas_weather.mvvm.utils.loadImage

class WeatherRecyclerAdapter(
        val itemClick: (Forecast) -> Unit
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
                val viewCurrent = parent.generateView(R.layout.list_item4)
                ViewHolderCurrent(viewCurrent)
            }
            is ViewType.Forecast -> {
                val viewForecast = parent.generateView(R.layout.list_item_layout2)
                ViewHolderForecast(viewForecast)
            }
            is ViewType.Further -> {
                val viewFurther = parent.generateView(R.layout.list_item3)
                ViewHolderFurtherDetails(viewFurther)
            }
        }
    }

    sealed class ViewType{
        object Empty : ViewType()
        object Current : ViewType()
        object Forecast : ViewType()
        object Further : ViewType()
    }

    private fun getDataType(type: Int): ViewType{
        return when (type){
            0 -> ViewType.Empty
            1 -> ViewType.Current
            2 -> ViewType.Forecast
            3 -> ViewType.Further
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (weather == null) return 0

        return when(position){
            0 -> 1
            in 1 until itemCount -2 -> 2
            itemCount - 1 -> 3
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
            is ViewType.Forecast -> {
                val viewHolderForecast = holder as ViewHolderForecast

                weather?.forecast?.get(position)?.let { i ->
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
        return 2 + (weather?.forecast?.size?: 0)
    }

    internal class ViewHolderCurrent(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        var locationTV: TextView = listItemView.findViewById(R.id.location_main_4)
        var conditionTV: TextView = listItemView.findViewById(R.id.condition_main_4)
        var weatherIV: ImageView = listItemView.findViewById(R.id.icon_main_4)
        var avgTempTV: TextView = listItemView.findViewById(R.id.temp_main_4)
        var tempUnit: TextView = listItemView.findViewById(R.id.temp_unit_4)

        fun bindData(weather: WeatherDisplay?){
            locationTV.text = weather?.location
            conditionTV.text = weather?.description
            weatherIV.loadImage(weather?.iconURL)
            avgTempTV.text = weather?.averageTemp?.toInt().toString()
            tempUnit.text = weather?.unit
        }
    }

    internal class ViewHolderForecast(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    internal class EmptyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}