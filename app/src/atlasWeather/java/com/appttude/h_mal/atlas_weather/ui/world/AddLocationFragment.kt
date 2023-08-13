package com.appttude.h_mal.atlas_weather.ui.world

import android.os.Bundle
import android.view.View
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.base.BaseFragment
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.goBack
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import kotlinx.android.synthetic.main.activity_add_forecast.location_name_tv
import kotlinx.android.synthetic.main.activity_add_forecast.submit


class AddLocationFragment : BaseFragment<WorldViewModel>(R.layout.activity_add_forecast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit.setOnClickListener {
            val locationName = location_name_tv.text?.trim()?.toString()
            if (locationName.isNullOrBlank()) {
                submit.error = "Location cannot be blank"
                return@setOnClickListener
            }
            viewModel.fetchDataForSingleLocationSearch(locationName)
        }
    }

    override fun onSuccess(data: Any?) {
        if (data is String) {
            displayToast(data)
            goBack()
        }
    }

}