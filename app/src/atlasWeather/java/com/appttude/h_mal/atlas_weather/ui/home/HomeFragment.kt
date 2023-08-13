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
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.ui.dialog.PermissionsDeclarationDialog
import com.appttude.h_mal.atlas_weather.ui.home.adapter.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
@RuntimePermissions
class HomeFragment : BaseFragment<MainViewModel>(R.layout.fragment_home) {

    lateinit var recyclerAdapter: WeatherRecyclerAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        swipe_refresh.apply {
            setOnRefreshListener {
                showLocationWithPermissionCheck()
                isRefreshing = true
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
        showLocationWithPermissionCheck()
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

    private fun navigateToFurtherDetails(forecast: Forecast) {
        val directions = HomeFragmentDirections.actionHomeFragmentToFurtherDetailsFragment(forecast)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(ACCESS_COARSE_LOCATION)
    fun showLocation() {
        viewModel.fetchData()
    }

    @OnShowRationale(ACCESS_COARSE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        PermissionsDeclarationDialog(requireContext()).showDialog({
            request.proceed()
        }, {
            request.cancel()
        })
    }

    @OnPermissionDenied(ACCESS_COARSE_LOCATION)
    fun onLocationDenied() {
        displayToast("Location permissions have been denied")
    }

    @OnNeverAskAgain(ACCESS_COARSE_LOCATION)
    fun onLocationNeverAskAgain() {
        displayToast("Location permissions have been to never ask again")
    }
}