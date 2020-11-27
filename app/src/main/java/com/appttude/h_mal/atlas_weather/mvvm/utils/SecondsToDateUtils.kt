package com.appttude.h_mal.atlas_weather.mvvm.utils


import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
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

fun String.changeDateFormat(): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
            val date = LocalDate.parse(this)
            date.format(formatter)
        } else {
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = format.parse(this)
            format = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
            format.format(date)

        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Unable to parse date"
    }

}

fun String.transformDateTimeString(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.ENGLISH)
        val dateIn = inputFormat.parse(substringBeforeLast(":"))
        outputFormat.format(dateIn)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

fun String.transformDateString(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
        val dateIn = inputFormat.parse(substringBeforeLast(":"))
        outputFormat.format(dateIn)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

fun String.transformPassportData(): String {
    return try {
        val formatIn = SimpleDateFormat("yyMMdd", Locale.ENGLISH)
        val formatOut = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val dateIn = formatIn.parse(this)
        formatOut.format(dateIn)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }

}

fun String.getYearsSinceNow(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
        val now = LocalDate.now()
        val date = LocalDate.parse(this, formatter)
        ChronoUnit.YEARS.between(date, now).toString()
    } else {
        val now = Calendar.getInstance()

        val date = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
        date.time = simpleDateFormat.parse(this)
        val years = now.get(Calendar.YEAR) - date.get(Calendar.YEAR)
        years.toString()
    }

}

fun String?.changeDateToSeconds(): Long {
//    if (isNullOrBlank()){
//        val time = System.currentTimeMillis() / 1000
//        return time + 2592000
//    }

    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val convertedCurrentDate = sdf.parse(this)
        convertedCurrentDate.time / 1000
    } catch (e: Exception) {
        val time = System.currentTimeMillis() / 1000
        time + 2592000
    }

}