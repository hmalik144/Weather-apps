package com.appttude.h_mal.atlas_weather.monoWeather.dialog

import android.content.Context
import android.os.Build
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.appttude.h_mal.atlas_weather.R


class PermissionsDeclarationDialog(context: Context) : BaseDeclarationDialog(context) {

    override val link: String = "https://sites.google.com/view/hmaldev/home/monochrome"
    override val message: String = "Hi, thank you for downloading my app. Google play isn't letting me upload my app to the Playstore until I have a privacy declaration :(. My app is basically used to demonstrate my code=ing to potential employers and others. I do NOT store or process any information. The location permission in the app is there just to provide the end user with weather data."
}

abstract class BaseDeclarationDialog(val context: Context): DeclarationBuilder {
    abstract override val link: String
    abstract override val message: String

    @RequiresApi(Build.VERSION_CODES.N)
    fun showDialog(agreeCallback: () -> Unit = { Unit }, disagreeCallback: () -> Unit = { Unit }) {
        val myMessage = buildMessage()

        val builder = AlertDialog.Builder(context)
                .setPositiveButton("agree") { _, _ ->
                    agreeCallback()
                }
                .setNegativeButton("disagree") { _, _ ->
                    disagreeCallback()
                }
                .setMessage(myMessage)
                .setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()

        // Make the textview clickable. Must be called after show()
        val msgTxt = alertDialog.findViewById<View>(R.id.message) as TextView?
        msgTxt!!.movementMethod = LinkMovementMethod.getInstance()
    }
}

