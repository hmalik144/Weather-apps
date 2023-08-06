package com.appttude.h_mal.data.network.response.forecast

import com.google.gson.annotations.SerializedName

data class Response(

		@field:SerializedName("current")
	val current: Current? = null,

		@field:SerializedName("timezone")
	val timezone: String? = null,

		@field:SerializedName("timezone_offset")
	val timezoneOffset: Int? = null,

		@field:SerializedName("daily")
	val daily: List<DailyItem?>? = null,

		@field:SerializedName("lon")
	val lon: Double? = null,

		@field:SerializedName("lat")
	val lat: Double? = null
)
