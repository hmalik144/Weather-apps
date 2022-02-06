package com.appttude.h_mal.atlas_weather.monoWeather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
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
class HomeFragment : BaseFragment() {

    private val viewModel by getFragmentViewModel<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WeatherRecyclerAdapter {
            val directions =
                    HomeFragmentDirections.actionHomeFragmentToFurtherDetailsFragment(it)
            navigateTo(directions)
        }

        forecast_listview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


        getPermissionResult(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST){
            viewModel.fetchData()
        }

        swipe_refresh.apply {
            setOnRefreshListener {
                getPermissionResult(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION_REQUEST){
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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.fetchData()
                displayToast("Permission granted")
            } else {
                displayToast("Permission denied")
            }
        }
    }
}