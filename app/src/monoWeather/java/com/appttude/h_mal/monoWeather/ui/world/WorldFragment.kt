package com.appttude.h_mal.monoWeather.ui.world

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.atlas_weather.ui.BaseFragment
import com.appttude.h_mal.monoWeather.ui.world.WorldFragmentDirections.actionWorldFragmentToWorldItemFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment__two.floatingActionButton
import kotlinx.android.synthetic.main.fragment__two.progressBar
import kotlinx.android.synthetic.main.fragment__two.world_recycler


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class WorldFragment : BaseFragment<WorldViewModel>(R.layout.fragment__two) {

    lateinit var recyclerAdapter: WorldRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchAllLocations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = WorldRecyclerAdapter({
            val direction =
                actionWorldFragmentToWorldItemFragment(it.location)
            navigateTo(direction)
        }) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete weather?")
                .setMessage("Are you sure want to delete $it?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteLocation(it)
                }
                .setNegativeButton("No", null)
                .create()
                .show()
        }

        world_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        floatingActionButton.setOnClickListener {
            navigateTo(R.id.action_worldFragment_to_addLocationFragment)
        }

    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)

        if (data is List<*>) {
            recyclerAdapter.addCurrent(data as List<WeatherDisplay>)
        }
    }

}