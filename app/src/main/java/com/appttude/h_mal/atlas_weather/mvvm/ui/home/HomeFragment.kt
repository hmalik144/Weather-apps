package com.appttude.h_mal.atlas_weather.mvvm.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.mvvm.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.mvvm.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.mvvm.ui.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.mvvm.utils.displayToast
import com.appttude.h_mal.atlas_weather.mvvm.utils.navigateTo
import com.appttude.h_mal.atlas_weather.mvvm.viewmodel.ApplicationViewModelFactory
import com.appttude.h_mal.atlas_weather.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_add_forecast.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ApplicationViewModelFactory>()

    private val viewModel by activityViewModels<MainViewModel> { factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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

        getPermissionResult(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_REQUEST){
            viewModel.fetchData()
        }

        swipe_refresh.apply {
            setOnRefreshListener {
                viewModel.fetchData()
                isRefreshing = true
            }
        }

        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.addCurrent(it)
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())

        viewModel.operationState.observe(viewLifecycleOwner){
            swipe_refresh.isRefreshing = false
        }

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