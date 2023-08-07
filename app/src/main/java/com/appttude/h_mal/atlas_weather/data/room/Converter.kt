package com.appttude.h_mal.atlas_weather.data.room

import android.content.Context
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@ProvidedTypeConverter
class Converter(context: Context) : KodeinAware {
    override val kodein by kodein(context)
    private val gson by instance<Gson>()

    @TypeConverter
    fun fullWeatherToString(fullWeather: FullWeather): String {
        return gson.toJson(fullWeather)
    }

    @TypeConverter
    fun stringToFullWeather(string: String): FullWeather {
        return gson.fromJson(string, FullWeather::class.java)
    }

}
