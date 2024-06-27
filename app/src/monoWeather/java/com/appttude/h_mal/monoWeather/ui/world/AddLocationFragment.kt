package com.appttude.h_mal.monoWeather.ui.world

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.goBack
import com.appttude.h_mal.atlas_weather.utils.hideKeyboard
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel




class AddLocationFragment : BaseFragment<WorldViewModel>(R.layout.activity_add_forecast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.submit).setOnClickListener {
            val locationNameView = view.findViewById<TextView>(R.id.location_name_tv)
            val locationName = locationNameView.text?.trim()?.toString()
            if (locationName.isNullOrBlank()) {
                locationNameView.error = "Location cannot be blank"
                return@setOnClickListener
            }
            viewModel.fetchDataForSingleLocationSearch(locationName)
            hideKeyboard()
        }
    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        if (data is String) {
            displayToast(data)
            goBack()
        }
    }
}