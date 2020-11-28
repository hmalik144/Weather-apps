package com.appttude.h_mal.atlas_weather.utils


import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

fun Int.toDayString(): String {
    return try {
        val date = Date(this.makeMilliseconds())
        val format = SimpleDateFormat("MMM d", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Unable to parse date"
    }
}

fun Int.makeMilliseconds(): Long = this * 1000L

fun Int.toDayName(): String {
    return try {
        val date = Date(this.makeMilliseconds())
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Unable to parse date"
    }
}

fun Int.toSmallDayName(): String {
    return try {
        val date = Date(this.makeMilliseconds())
        val format = SimpleDateFormat("EEE", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Unable to parse date"
    }
}

fun Int?.toTime(): String? {
    return this?.makeMilliseconds()?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            val date = Date(it)
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            format.format(date)
        }
    }
}




