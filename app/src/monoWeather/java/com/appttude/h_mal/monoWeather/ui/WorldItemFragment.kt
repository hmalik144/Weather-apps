package com.appttude.h_mal.monoWeather.ui


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.monoWeather.ui.home.adapter.WeatherRecyclerAdapter




class WorldItemFragment : BaseFragment<WorldViewModel>(R.layout.fragment_home) {

    private var retrievedLocationName: String? = null

    private lateinit var recyclerAdapter: WeatherRecyclerAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrievedLocationName = WorldItemFragmentArgs.fromBundle(requireArguments()).locationName
        retrievedLocationName?.let { viewModel.setLocation(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = WeatherRecyclerAdapter {
            val directions = WorldItemFragmentDirections
                .actionWorldItemFragmentToFurtherDetailsFragment(it)
            navigateTo(directions)
        }

        view.findViewById<RecyclerView>(R.id.forecast_listview).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).apply {
            setOnRefreshListener {
                retrievedLocationName?.let {
                    viewModel.fetchDataForSingleLocation(it)
                    isRefreshing = true
                }
            }
        }

        retrievedLocationName?.let { viewModel.getSingleLocation(it) }
    }

    override fun onSuccess(data: Any?) {
        if (data is WeatherDisplay) {
            recyclerAdapter.addCurrent(data)
        }
        super.onSuccess(data)
        swipeRefresh.isRefreshing = false
    }

    override fun onFailure(error: Any?) {
        super.onFailure(error)
        swipeRefresh.isRefreshing = false
    }
}