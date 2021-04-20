package com.appttude.h_mal.atlas_weather.monoWeather.ui.world

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.monoWeather.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.goBack
import com.appttude.h_mal.atlas_weather.utils.hideKeyboard
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import kotlinx.android.synthetic.main.activity_add_forecast.*


class AddLocationFragment : BaseFragment() {

    private val viewModel by getFragmentViewModel<WorldViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_add_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit.setOnClickListener {
            val locationName = location_name_tv.text?.trim()?.toString()
            if (locationName.isNullOrBlank()){
                location_name_tv.error = "Location cannot be blank"
                return@setOnClickListener
            }
            viewModel.fetchDataForSingleLocationSearch(locationName)
            hideKeyboard()
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())

        viewModel.operationComplete.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { message ->
                displayToast(message)
            }
            goBack()

        }
    }
}