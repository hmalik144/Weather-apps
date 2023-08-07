package com.appttude.h_mal.atlas_weather.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather
import com.appttude.h_mal.atlas_weather.utils.displayToast
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

/**
 * Created by h_mal on 29/04/2018.
 * Updated by h_mal on 27/11/2020
 */
const val NOTIFICATION_CHANNEL_ID = "my_notification_channel_1"

class NotificationReceiver : BroadcastReceiver() {

    private val kodein = LateInitKodein()
    private val helper: ServicesHelper by kodein.instance()

    override fun onReceive(context: Context, intent: Intent) {
        kodein.baseKodein = (context.applicationContext as KodeinAware).kodein

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            context.displayToast("Please enable location permissions")
            return
        }

        // notification validation
    }

    private fun pushNotif(context: Context?, weather: FullWeather) {
        val notificationIntent = Intent(context, MainActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(MainActivity::class.java)
            addNextIntent(notificationIntent)
        }

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }

        val notification = builder.setContentTitle("Weather App")
                .setContentText(weather.current?.main + "°C")
                .setSmallIcon(R.mipmap.ic_notif) //change icon
//                .setLargeIcon(Icon.createWithResource(context, getImageResource(forecastItem.getCurrentForecast().getIconURL(), context)))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        }
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}