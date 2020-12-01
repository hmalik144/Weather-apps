package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.further

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.generateView
import kotlinx.android.synthetic.monoWeather.mono_item_two_cell.view.*


class GridAdapter(
        context: Context,
        list: List<Pair<Int, String>>
) : ArrayAdapter<Pair<Int, String>>(context, 0, list){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: parent.generateView(R.layout.mono_item_two_cell)

        val item = getItem(position)

        return view.apply {
            mono_item_cell.setImageResource(item!!.first)
            mono_text_cell.text = item.second
        }
    }
}