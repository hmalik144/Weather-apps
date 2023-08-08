package com.appttude.h_mal.atlas_weather.ui.world

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.monoWeather.ui.BaseFragment
import kotlinx.android.synthetic.atlasWeather.fragment_add_location.floatingActionButton
import kotlinx.android.synthetic.atlasWeather.fragment_add_location.progressBar2
import kotlinx.android.synthetic.atlasWeather.fragment_add_location.world_recycler


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class WorldFragment : BaseFragment(R.layout.fragment_add_location) {
    val viewModel by getFragmentViewModel<WorldViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WorldRecyclerAdapter {
            val direction =
                WorldFragmentDirections.actionWorldFragmentToWorldItemFragment(it)
            navigateTo(direction)
        }

        world_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.addCurrent(it)
        }

        floatingActionButton.setOnClickListener {
            navigateTo(R.id.action_worldFragment_to_addLocationFragment)
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar2))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())

    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchAllLocations()
    }

}