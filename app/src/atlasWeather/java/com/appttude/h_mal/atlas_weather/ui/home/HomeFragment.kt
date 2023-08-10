package com.appttude.h_mal.atlas_weather.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.ui.home.adapter.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<MainViewModel>(R.layout.fragment_home) {
    private lateinit var recyclerAdapter: WeatherRecyclerAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        recyclerAdapter = WeatherRecyclerAdapter(itemClick = {
            navigateToFurtherDetails(it)
        })

        forecast_listview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        swipe_refresh.apply {
            setOnRefreshListener {
                getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
                    viewModel.fetchData()
                    isRefreshing = true
                }
            }
        }
    }

    override fun onFailure(error: Any?) {
        swipe_refresh.isRefreshing = false
    }

    override fun onSuccess(data: Any?) {
        swipe_refresh.isRefreshing = false
        if (data is WeatherDisplay) {
            recyclerAdapter.addCurrent(data)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()

        getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
            viewModel.fetchData()
        }
    }

    @SuppressLint("MissingPermission")
    override fun permissionsGranted() {
        viewModel.fetchData()
    }

    private fun navigateToFurtherDetails(forecast: Forecast) {
        val directions = HomeFragmentDirections
            .actionHomeFragmentToFurtherDetailsFragment(forecast)
        navigateTo(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(requireActivity(), R.id.container)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}