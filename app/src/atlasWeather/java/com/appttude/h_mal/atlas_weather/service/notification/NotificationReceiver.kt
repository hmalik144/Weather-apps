package com.appttude.h_mal.atlas_weather.service.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
const val NOTIFICATION_ID = 505
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
            val bmp: Bitmap = runBlocking { Picasso.get().load(weather.current?.icon).get() }

            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_notif)
                .setLargeIcon(bmp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is not in the Support Library.
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                    //                                        grantResults: IntArray)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return@with
                }
                // notificationId is a unique int for each notification that you must define.
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}