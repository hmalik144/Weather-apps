package com.appttude.h_mal.atlas_weather.model.weather

import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse

data class FullWeather(
		val current: Current? = null,
		val timezone: String? = null,
		val timezoneOffset: Int? = null,
		val daily: List<DailyWeather>? = null,
		val lon: Double = 0.00,
		val lat: Double = 0.00
) {

	constructor(weatherResponse: WeatherResponse): this(
			weatherResponse.current?.let { Current(it) },
			weatherResponse.timezone,
			weatherResponse.timezoneOffset,
			weatherResponse.daily?.map { DailyWeather(it)  },
			weatherResponse.lon,
			weatherResponse.lat
	)

}


