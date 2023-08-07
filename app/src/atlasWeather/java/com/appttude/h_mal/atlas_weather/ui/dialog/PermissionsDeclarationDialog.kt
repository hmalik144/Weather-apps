package com.appttude.h_mal.atlas_weather.ui.dialog

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class PermissionsDeclarationDialog(context: Context) : BaseDeclarationDialog(context) {

    override val link: String = "https://sites.google.com/view/hmaldev/home/monochrome"
    override val message: String = "Hi, thank you for downloading my app. Google play isn't letting me upload my app to the Playstore until I have a privacy declaration :(. My app is basically used to demonstrate my code=ing to potential employers and others. I do NOT store or process any information. The location permission in the app is there just to provide the end user with weather data."
}

abstract class BaseDeclarationDialog(val context: Context): DeclarationBuilder {
    abstract override val link: String
    abstract override val message: String

    lateinit var dialog: AlertDialog

    fun showDialog(agreeCallback: () -> Unit = { }, disagreeCallback: () -> Unit = { }) {
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

        dialog = builder.create()
        dialog.show()

        // Make the textview clickable. Must be called after show()
        val msgTxt = dialog.findViewById<View>(android.R.id.message) as TextView?
        msgTxt?.movementMethod = LinkMovementMethod.getInstance()
    }

    fun dismiss() = dialog.dismiss()
}

