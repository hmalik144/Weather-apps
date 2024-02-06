package com.appttude.h_mal.atlas_weather.service.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private val helper: NotificationHelper by kodein.instance()

    override fun onReceive(context: Context, intent: Intent) {
        kodein.baseKodein = (context.applicationContext as KodeinAware).kodein

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pushNotification(context)
        } else {
            context.displayToast("Please enable location permissions")
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun pushNotification(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Retrieve weather data
            val weather = runBlocking { helper.fetchData() } ?: return@launch

            // Build notification
            val notificationIntent = Intent(context, MainActivity::class.java)

            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(notificationIntent)
            }
            val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            val bmp: Bitmap = runBlocking { Picasso.get().load(weather.current?.icon).get() }

            val notification = builder.setContentTitle("Weather App")
                .setContentText(weather.current?.main + "°C")
                .setSmallIcon(R.mipmap.ic_notif) //change icon
                .setLargeIcon(bmp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)

            // Deliver notification
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
    }
}