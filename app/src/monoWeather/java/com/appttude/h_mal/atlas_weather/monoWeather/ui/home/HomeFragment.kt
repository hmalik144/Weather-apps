package com.appttude.h_mal.atlas_weather.monoWeather.ui.home

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.monoWeather.dialog.PermissionsDeclarationDialog
import com.appttude.h_mal.atlas_weather.monoWeather.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel by getFragmentViewModel<MainViewModel>()

    lateinit var dialog: PermissionsDeclarationDialog

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WeatherRecyclerAdapter(itemClick = {
            navigateToFurtherDetails(it)
        })

        forecast_listview.adapter = recyclerAdapter


        dialog = PermissionsDeclarationDialog(requireContext())
        dialog.showDialog(agreeCallback = {
            getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
                viewModel.fetchData()
            }
        })

        swipe_refresh.apply {
            setOnRefreshListener {
                getPermissionResult(ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST) {
                    viewModel.fetchData()
                    isRefreshing = true
                }
            }
        }

        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.addCurrent(it)
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())
        viewModel.operationRefresh.observe(viewLifecycleOwner, refreshObserver(swipe_refresh))
    }

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

    @SuppressLint("MissingPermission")
    override fun permissionsGranted() {
        viewModel.fetchData()
    }

    private fun navigateToFurtherDetails(forecast: Forecast){
        val directions = HomeFragmentDirections
                .actionHomeFragmentToFurtherDetailsFragment(forecast)
        navigateTo(directions)
    }
}