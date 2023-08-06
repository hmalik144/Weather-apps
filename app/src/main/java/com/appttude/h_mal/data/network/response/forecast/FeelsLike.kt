package com.appttude.h_mal.data.network.response.forecast

import com.google.gson.annotations.SerializedName

data class FeelsLike(

	@field:SerializedName("eve")
	val eve: Double? = null,

	@field:SerializedName("night")
	val night: Double? = null,

	@field:SerializedName("day")
	val day: Double? = null,

	@field:SerializedName("morn")
	val morn: Double? = null
)
