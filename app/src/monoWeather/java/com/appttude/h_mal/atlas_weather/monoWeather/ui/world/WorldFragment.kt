package com.appttude.h_mal.atlas_weather.monoWeather.ui.world

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.monoWeather.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.monoWeather.ui.world.WorldFragmentDirections
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_add_location.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class WorldFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ApplicationViewModelFactory>()

    val viewModel by viewModels<WorldViewModel> { factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WorldRecyclerAdapter({
            val direction =
                    WorldFragmentDirections.actionWorldFragmentToWorldItemFragment(it)
            navigateTo(direction)
        }){
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete weather?")
                    .setMessage("Are you sure want to delete $it?")
                    .setPositiveButton("Yes"){ _, _ ->
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

        floatingActionButton.setOnClickListener{
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