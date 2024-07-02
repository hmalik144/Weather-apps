package com.appttude.h_mal.atlas_weather.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast



private const val WEATHER = "param1"

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

        view.findViewById<TextView>(R.id.maxtemp).text = param1?.mainTemp
        view.findViewById<TextView>(R.id.averagetemp).text = param1?.averageTemp
        view.findViewById<TextView>(R.id.minimumtemp).text = param1?.minorTemp
        view.findViewById<TextView>(R.id.windtext).text = param1?.windText
        view.findViewById<TextView>(R.id.preciptext).text = param1?.precipitation
        view.findViewById<TextView>(R.id.sunrisetext).text = param1?.sunrise
        view.findViewById<TextView>(R.id.sunsettext).text = param1?.sunset
    }
}