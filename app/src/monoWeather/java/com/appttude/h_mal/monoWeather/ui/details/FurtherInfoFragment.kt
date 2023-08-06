package com.appttude.h_mal.monoWeather.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import kotlinx.android.synthetic.main.activity_further_info.*


/**
 * A simple [Fragment] subclass.
 * Use the [FurtherInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FurtherInfoFragment : Fragment() {
    private var param1: Forecast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = FurtherInfoFragmentArgs.fromBundle(requireArguments()).forecast

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_further_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maxtemp.text = param1?.mainTemp.appendWith(requireContext().getString(R.string.degrees))
        averagetemp.text = param1?.averageTemp.appendWith(requireContext().getString(R.string.degrees))
        minimumtemp.text = param1?.minorTemp.appendWith(requireContext().getString(R.string.degrees))
        windtext.text = param1?.windText.appendWith(" km")
        preciptext.text = param1?.precipitation.appendWith(" %")
        cloudtext.text =   param1?.cloud.appendWith(" %")
        humiditytext.text = param1?.humidity.appendWith(" %")
        uvtext.text = param1?.uvi
        sunrisetext.text = param1?.sunrise
        sunsettext.text = param1?.sunset
    }

    fun String?.appendWith(suffix: String): String?{
        return this?.let {
            StringBuilder().append(it).append(suffix).toString()
        }
    }
}