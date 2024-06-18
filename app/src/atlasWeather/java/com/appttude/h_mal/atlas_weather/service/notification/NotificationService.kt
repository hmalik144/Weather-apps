package com.appttude.h_mal.atlas_weather.service.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import androidx.core.app.NotificationManagerCompat


class NotificationService(context: Context) {

    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    private val alarmPendingIntent by lazy {
        val intent = Intent(context, NotificationReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    fun schedulePushNotifications() {
        val calendar = getCalendarForNotification()

        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
            alarmPendingIntent
        )

//        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, alarmPendingIntent), alarmPendingIntent)
    }

    fun unschedulePushNotifications() {
        alarmManager.cancel(alarmPendingIntent)
    }

    fun areNotificationsEnabled() = when {
        notificationManager.areNotificationsEnabled().not() -> false
        else -> {
            notificationManager.notificationChannels.firstOrNull { channel ->
                channel.importance == NotificationManager.IMPORTANCE_NONE
            } == null
        }
    }

    private fun getCalendarForNotification(): Calendar {
//        return GregorianCalendar.getInstance().apply {
//            if (get(Calendar.HOUR_OF_DAY) >= HOUR_TO_SHOW_PUSH) {
//                add(Calendar.DAY_OF_MONTH, 1)
//            }
//
//            set(Calendar.HOUR_OF_DAY, HOUR_TO_SHOW_PUSH)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }

        return GregorianCalendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        }
    }
}