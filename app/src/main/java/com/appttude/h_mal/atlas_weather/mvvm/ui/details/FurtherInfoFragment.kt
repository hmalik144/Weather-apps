package com.appttude.h_mal.atlas_weather.mvvm.ui.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.mvvm.model.forecast.Forecast
import kotlinx.android.synthetic.main.activity_further_info.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_further_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maxtemp.text = param1?.mainTemp
        averagetemp.text = param1?.averageTemp
        minimumtemp.text = param1?.minorTemp
        windtext.text = param1?.windText
        preciptext.text = param1?.precipitation
        humiditytext.text = param1?.humidity
        uvtext.text = param1?.uvi
        sunrisetext.text = param1?.sunrise
        sunsettext.text = param1?.sunset
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment FurtherInfoFragment.
         */
        @JvmStatic
        fun newInstance(param1: Parcelable) =
                FurtherInfoFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(WEATHER, param1)
                    }
                }
    }
}