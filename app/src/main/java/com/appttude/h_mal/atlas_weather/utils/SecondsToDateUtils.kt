package com.appttude.h_mal.atlas_weather.utils


import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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
        OffsetTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}




