package com.appttude.h_mal.monoWeather.ui.world

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.monoWeather.ui.BaseFragment
import com.appttude.h_mal.monoWeather.ui.world.WorldFragmentDirections.actionWorldFragmentToWorldItemFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment__two.floatingActionButton
import kotlinx.android.synthetic.main.fragment__two.progressBar
import kotlinx.android.synthetic.main.fragment__two.world_recycler


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class WorldFragment : BaseFragment(R.layout.fragment__two) {
    private val viewModel by getFragmentViewModel<WorldViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchAllLocations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WorldRecyclerAdapter({
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

        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.addCurrent(it)
        }

        floatingActionButton.setOnClickListener {
            navigateTo(R.id.action_worldFragment_to_addLocationFragment)
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())

    }

}