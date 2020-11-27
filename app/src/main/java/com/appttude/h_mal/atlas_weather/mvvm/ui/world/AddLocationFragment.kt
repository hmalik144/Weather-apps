package com.appttude.h_mal.atlas_weather.mvvm.ui.world

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.mvvm.ui.BaseFragment
import com.appttude.h_mal.atlas_weather.mvvm.utils.displayToast
import com.appttude.h_mal.atlas_weather.mvvm.utils.goBack
import com.appttude.h_mal.atlas_weather.mvvm.viewmodel.ApplicationViewModelFactory
import com.appttude.h_mal.atlas_weather.mvvm.viewmodel.WorldViewModel
import kotlinx.android.synthetic.main.activity_add_forecast.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AddLocationFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ApplicationViewModelFactory>()

    private val viewModel by viewModels<WorldViewModel> { factory }

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