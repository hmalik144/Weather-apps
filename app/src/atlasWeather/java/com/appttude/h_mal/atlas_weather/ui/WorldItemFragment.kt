package com.appttude.h_mal.atlas_weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.ui.home.adapter.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.utils.navigateTo



class WorldItemFragment : Fragment() {
    private var param1: WeatherDisplay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = WorldItemFragmentArgs.fromBundle(requireArguments()).weatherDisplay
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WeatherRecyclerAdapter {
            val directions =
                WorldItemFragmentDirections.actionWorldItemFragmentToFurtherDetailsFragment(it)
            navigateTo(directions)
        }

        param1?.let { recyclerAdapter.addCurrent(it) }

        view.findViewById<RecyclerView>(R.id.forecast_listview).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

    }

}