package com.appttude.h_mal.monoWeather.ui


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.monoWeather.ui.home.adapter.WeatherRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.forecast_listview
import kotlinx.android.synthetic.main.fragment_home.swipe_refresh


class WorldItemFragment : BaseFragment<WorldViewModel>(R.layout.fragment_home) {

    private var param1: String? = null

    private lateinit var recyclerAdapter: WeatherRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = WorldItemFragmentArgs.fromBundle(requireArguments()).locationName
        param1?.let { viewModel.setLocation(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = WeatherRecyclerAdapter {
            val directions =
                WorldItemFragmentDirections.actionWorldItemFragmentToFurtherDetailsFragment(it)
            navigateTo(directions)
        }

        forecast_listview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        swipe_refresh.apply {
            setOnRefreshListener {
                param1?.let {
                    viewModel.fetchDataForSingleLocation(it)
                    isRefreshing = true
                }
            }
        }

        param1?.let { viewModel.getSingleLocation(it) }
    }

    override fun onSuccess(data: Any?) {
        if (data is WeatherDisplay) {
            recyclerAdapter.addCurrent(data)
        }
        super.onSuccess(data)
        swipe_refresh.isRefreshing = false
    }

    override fun onFailure(error: Any?) {
        super.onFailure(error)
        swipe_refresh.isRefreshing = false
    }
}