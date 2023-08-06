package com.appttude.h_mal.monoWeather.ui.world

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import com.appttude.h_mal.R
import com.appttude.h_mal.monoWeather.ui.BaseFragment
import com.appttude.h_mal.utils.displayToast
import com.appttude.h_mal.utils.goBack
import com.appttude.h_mal.utils.hideKeyboard
import com.appttude.h_mal.viewmodel.WorldViewModel
import kotlinx.android.synthetic.main.activity_add_forecast.*


class AddLocationFragment : BaseFragment(R.layout.activity_add_forecast) {

    private val viewModel by getFragmentViewModel<WorldViewModel>()

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