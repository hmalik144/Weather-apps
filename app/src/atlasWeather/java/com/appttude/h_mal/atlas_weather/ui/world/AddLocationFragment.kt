package com.appttude.h_mal.atlas_weather.ui.world

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.goBack
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import com.appttude.h_mal.monoWeather.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_add_forecast.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AddLocationFragment : BaseFragment(R.layout.activity_add_forecast) {

    private val viewModel by getFragmentViewModel<WorldViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit.setOnClickListener {
            val locationName = location_name_tv.text?.trim()?.toString()
            if (locationName.isNullOrBlank()){
                submit.error = "Location cannot be blank"
                return@setOnClickListener
            }
            viewModel.fetchDataForSingleLocation(locationName)
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())

        viewModel.operationComplete.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {message ->
                displayToast(message)
            }
            goBack()
        }
    }

}