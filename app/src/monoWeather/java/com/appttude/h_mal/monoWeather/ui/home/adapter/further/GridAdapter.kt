package com.appttude.h_mal.monoWeather.ui.home.adapter.further

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.generateView




class GridAdapter(
    context: Context,
    list: List<Pair<Int, String>>
) : ArrayAdapter<Pair<Int, String>>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: parent.generateView(R.layout.mono_item_two_cell)

        val item = getItem(position)

        return view.apply {
            findViewById<ImageView>(R.id.mono_item_cell).setImageResource(item!!.first)
            findViewById<TextView>(R.id.mono_text_cell).text = item.second
        }
    }
}