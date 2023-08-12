package com.appttude.h_mal.monoWeather.ui.home

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
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.MainViewModel
import com.appttude.h_mal.monoWeather.dialog.PermissionsDeclarationDialog
import com.appttude.h_mal.atlas_weather.ui.BaseFragment
import com.appttude.h_mal.monoWeather.ui.home.adapter.WeatherRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<MainViewModel>(R.layout.fragment_home) {

    lateinit var dialog: PermissionsDeclarationDialog
    lateinit var recyclerAdapter: WeatherRecyclerAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        dialog = PermissionsDeclarationDialog(requireContext())

        swipe_refresh.apply {
            setOnRefreshListener {
                getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
                    viewModel.fetchData()
                    isRefreshing = true
                }
            }
        }

        recyclerAdapter = WeatherRecyclerAdapter(itemClick = {
            navigateToFurtherDetails(it)
        })

        forecast_listview.adapter = recyclerAdapter
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        dialog.showDialog(agreeCallback = {
            getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
                viewModel.fetchData()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        swipe_refresh.isRefreshing = false

        if (data is WeatherDisplay) {
            recyclerAdapter.addCurrent(data)
        }
    }

    override fun onFailure(error: Any?) {
        super.onFailure(error)
        swipe_refresh.isRefreshing = false
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