package com.appttude.h_mal.atlas_weather.mvvm.model.weather

import com.appttude.h_mal.atlas_weather.mvvm.data.network.response.forecast.Current

data class Current(
		val dt: Int? = null,
		val sunrise: Int? = null,
		val sunset: Int? = null,
		val temp: Double? = null,
		val visibility: Int? = null,
		val uvi: Double? = null,
		val pressure: Int? = null,
		val clouds: Int? = null,
		val feelsLike: Double? = null,
		val windDeg: Int? = null,
		val dewPoint: Double? = null,
		val icon: String? = null,
		val description: String? = null,
		val main: String? = null,
		val id: Int? = null,
		val humidity: Int? = null,
		val windSpeed: Double? = null
){

	constructor(dailyItem: Current): this(
			dailyItem.dt,
			dailyItem.sunrise,
			dailyItem.sunset,
			dailyItem.temp,
			dailyItem.visibility,
			dailyItem.uvi,
			dailyItem.pressure,
			dailyItem.clouds,
			dailyItem.feelsLike,
			dailyItem.windDeg,
			dailyItem.dewPoint,
			dailyItem.weather?.get(0)?.icon?.let { "https://openweathermap.org/img/wn/${it}@4x.png" },
			dailyItem.weather?.get(0)?.description,
			dailyItem.weather?.get(0)?.main,
			dailyItem.weather?.get(0)?.id,
			dailyItem.humidity,
			dailyItem.windSpeed
	)
}