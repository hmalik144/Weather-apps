package com.appttude.h_mal.monoWeather.ui.widget

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.monoWeather.dialog.DeclarationBuilder
import com.appttude.h_mal.monoWeather.dialog.PermissionsDeclarationDialog
import kotlinx.android.synthetic.monoWeather.permissions_declaration_dialog.cancel
import kotlinx.android.synthetic.monoWeather.permissions_declaration_dialog.submit
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class WidgetLocationPermissionActivity : AppCompatActivity(), DeclarationBuilder {
    override val link: String = "https://sites.google.com/view/hmaldev/home/monochrome"
    override var message: String = ""

    private var mAppWidgetId = INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        message = readFromResources(R.string.widget_declaration)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        // Find the widget id from the intent.
        intent.extras?.let {
            mAppWidgetId = it.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContentView(R.layout.permissions_declaration_dialog)
        findViewById<TextView>(R.id.declaration_text).apply {
            text = buildMessage()
            movementMethod = LinkMovementMethod.getInstance()
        }

        submit.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                showBackgroundLocationWithPermissionCheck()
            } else {
                showLocationWithPermissionCheck()
            }
        }

        cancel.setOnClickListener { finish() }
    }

    private fun submitWidget() {
        sendUpdateIntent()
        finishCurrencyWidgetActivity()
    }

    private fun finishCurrencyWidgetActivity() {
        // Make sure we pass back the original appWidgetId
        val resultValue = intent
        resultValue.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    private fun sendUpdateIntent() {
        // It is the responsibility of the configuration activity to update the app widget
        // Send update broadcast to widget app class
        Intent(
            this@WidgetLocationPermissionActivity,
            WidgetLocationPermissionActivity::class.java
        ).apply {
            action = ACTION_APPWIDGET_UPDATE

            // Put current app widget ID into extras and send broadcast
            putExtra(EXTRA_APPWIDGET_IDS, intArrayOf(mAppWidgetId))
            sendBroadcast(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(ACCESS_COARSE_LOCATION)
    fun showLocation() {
        submitWidget()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @NeedsPermission(ACCESS_BACKGROUND_LOCATION)
    fun showBackgroundLocation() {
        submitWidget()
    }

    @OnShowRationale(ACCESS_COARSE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        PermissionsDeclarationDialog(this).showDialog({
            request.proceed()
        }, {
            request.cancel()
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnShowRationale(ACCESS_BACKGROUND_LOCATION)
    fun showRationaleForBackgroundLocation(request: PermissionRequest) {
        PermissionsDeclarationDialog(this).showDialog({
            request.proceed()
        }, {
            request.cancel()
        })
    }

    @OnPermissionDenied(ACCESS_COARSE_LOCATION)
    fun onLocationDenied() {
        displayToast("Location permissions have been denied")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnPermissionDenied(ACCESS_BACKGROUND_LOCATION)
    fun onBackgroundLocationDenied() {
        displayToast("Background Location permissions have been denied")
    }

    @OnNeverAskAgain(ACCESS_COARSE_LOCATION)
    fun onLocationNeverAskAgain() {
        displayToast("Location permissions have been to never ask again")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnNeverAskAgain(ACCESS_BACKGROUND_LOCATION)
    fun onBackgroundLocationNeverAskAgain() {
        displayToast("Background Location permissions have been to never ask again")
    }
}