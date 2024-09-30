package com.appttude.h_mal.atlas_weather.data.network.response.weather

import com.appttude.h_mal.atlas_weather.EGLL
import com.appttude.h_mal.atlas_weather.EGWU
import com.appttude.h_mal.atlas_weather.F8628
import com.google.gson.annotations.SerializedName


data class Stations (
    @SerializedName("EGWU"  ) var EGWU  : EGWU?  = EGWU(),
    @SerializedName("EGLC"  ) var EGLC  : EGLC?  = EGLC(),
    @SerializedName("EGLL"  ) var EGLL  : EGLL?  = EGLL(),
    @SerializedName("D5621" ) var D5621 : D5621? = D5621(),
    @SerializedName("F8628" ) var F8628 : F8628? = F8628()
)