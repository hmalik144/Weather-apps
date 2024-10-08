package com.appttude.h_mal.atlas_weather.ui.world

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class WorldFragment : BaseFragment<WorldViewModel>(R.layout.fragment_add_location) {
    private lateinit var recyclerAdapter: WorldRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = WorldRecyclerAdapter {
            val direction =
                WorldFragmentDirections.actionWorldFragmentToWorldItemFragment(it)
            navigateTo(direction)
        }

        view.findViewById<RecyclerView>(R.id.world_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            navigateTo(R.id.action_worldFragment_to_addLocationFragment)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        if (data is List<*>)
            recyclerAdapter.addCurrent(data as List<WeatherDisplay>)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllLocations()
    }

}