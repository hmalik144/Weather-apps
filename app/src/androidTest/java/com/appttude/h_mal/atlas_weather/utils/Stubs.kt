package com.appttude.h_mal.atlas_weather.utils

enum class Stubs(
    val id: String
) {
    Metric("valid_response_metric"),
    Imperial("valid_response_imperial"),
    WrongLocation("wrong_location_response"),
    InvalidKey("invalid_api_key_response")
}