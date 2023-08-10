package com.appttude.h_mal.monoWeather.ui.world

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.goBack
import com.appttude.h_mal.atlas_weather.utils.hideKeyboard
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.atlas_weather.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_add_forecast.location_name_tv
import kotlinx.android.synthetic.main.activity_add_forecast.progressBar
import kotlinx.android.synthetic.main.activity_add_forecast.submit


class AddLocationFragment : BaseFragment<WorldViewModel>(R.layout.activity_add_forecast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit.setOnClickListener {
            val locationName = location_name_tv.text?.trim()?.toString()
            if (locationName.isNullOrBlank()) {
                location_name_tv.error = "Location cannot be blank"
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