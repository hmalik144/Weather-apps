package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.utils.generateIconUrlString
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.Hour as ForecastHour


data class Hour(
		val dt: Int? = null,
		val temp: Double? = null,
		val icon: String? = null
){
	constructor(hour: ForecastHour) : this(
			hour.dt,
			hour.temp,
			generateIconUrlString(hour.weather?.getOrNull(0)?.icon)
	)
}


