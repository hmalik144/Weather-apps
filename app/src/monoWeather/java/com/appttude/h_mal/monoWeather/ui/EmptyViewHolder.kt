package com.appttude.h_mal.monoWeather.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.icon)
    var bodyTV: TextView = itemView.findViewById(R.id.body_text)
    var headerTV: TextView = itemView.findViewById(R.id.header_text)

    fun bindData(@DrawableRes imageRes: Int?, header: String, body: String) {
        imageRes?.let { icon.setImageResource(it) }
        headerTV.text = header
        bodyTV.text = body

    }
}