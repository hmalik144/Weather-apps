package com.appttude.h_mal.atlas_weather.monoWeather.dialog

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface DeclarationBuilder{
    val link: String
    val message: String

    fun Context.readFromResources(@StringRes id: Int) = resources.getString(id)

    @RequiresApi(Build.VERSION_CODES.N)
    fun buildMessage(): CharSequence? {
        val link1 = "<font color='blue'><a href=\"$link\">here</a></font>"
        val message = "$message See my privacy policy: $link1"
        return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY)
    }
}