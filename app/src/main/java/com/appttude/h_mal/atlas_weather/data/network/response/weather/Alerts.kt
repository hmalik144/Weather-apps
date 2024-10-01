package com.appttude.h_mal.atlas_weather.data.network.response.weather

import com.google.gson.annotations.SerializedName

data class Alerts(
    @SerializedName("event") var event: String? = null,
    @SerializedName("headline") var headline: String? = null,
    @SerializedName("ends") var ends: String? = null,
    @SerializedName("endsEpoch") var endsEpoch: Int? = null,
    @SerializedName("onset") var onset: String? = null,
    @SerializedName("onsetEpoch") var onsetEpoch: Int? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("link") var link: String? = null,
    @SerializedName("description") var description: String? = null,
)
