package com.appttude.h_mal.monoWeather.dialog

import android.content.Context
import android.text.Html
import androidx.annotation.StringRes

interface DeclarationBuilder {
    val link: String
    val message: String

    fun Context.readFromResources(@StringRes id: Int) = resources.getString(id)

    fun buildMessage(): CharSequence? {
        val link1 = "<font color='blue'><a href=\"$link\">here</a></font>"
        val message = "$message See my privacy policy: $link1"
        return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY)
    }
}