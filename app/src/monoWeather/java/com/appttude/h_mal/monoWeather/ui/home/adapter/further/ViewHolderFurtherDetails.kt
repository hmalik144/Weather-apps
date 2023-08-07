package com.appttude.h_mal.monoWeather.ui.home.adapter.further

import android.view.View
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay

class ViewHolderFurtherDetails(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var grid: GridView = itemView.findViewById(R.id.grid_mono)

    fun bindData(weather: WeatherDisplay?){
        grid.adapter = GridAdapter(itemView.context, listOf<Pair<Int, String>>(
                Pair(R.drawable.breeze,"${weather?.windSpeed ?: "0"} km"),
                Pair(R.drawable.water_drop,"${weather?.precipitation ?: "0"} %" ),
                Pair(R.drawable.cloud_symbol,"${weather?.clouds ?: "0"} %" )
        ))

    }
}