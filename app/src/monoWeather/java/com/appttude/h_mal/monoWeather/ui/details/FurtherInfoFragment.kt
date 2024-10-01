package com.appttude.h_mal.monoWeather.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_further_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.maxtemp).text = param1?.mainTemp.appendWith(requireContext().getString(R.string.degrees))
        view.findViewById<TextView>(R.id.averagetemp).text =
            param1?.averageTemp.appendWith(requireContext().getString(R.string.degrees))
        view.findViewById<TextView>(R.id.minimumtemp).text =
            param1?.minorTemp.appendWith(requireContext().getString(R.string.degrees))
                    view.findViewById<TextView>(R.id.windtext).text = param1?.windText.appendWith(" km")
                    view.findViewById<TextView>(R.id.preciptext).text = param1?.precipitation.appendWith(" %")
                    view.findViewById<TextView>(R.id.cloudtext).text = param1?.cloud.appendWith(" %")
                    view.findViewById<TextView>(R.id.humiditytext).text = param1?.humidity.appendWith(" %")
                    view.findViewById<TextView>(R.id.uvtext).text = param1?.uvi
                    view.findViewById<TextView>(R.id.sunrisetext).text = param1?.sunrise
                    view.findViewById<TextView>(R.id.sunsettext).text = param1?.sunset
    }

    fun String?.appendWith(suffix: String): String? {
        return this?.let {
            StringBuilder().append(it).append(suffix).toString()
        }
    }
}