package com.appttude.h_mal.utils

import android.os.Parcel
import android.os.Parcelable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

inline fun <reified T> parcelableCreator(
        crossinline create: (Parcel) -> T) =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel) = create(source)
            override fun newArray(size: Int) = arrayOfNulls<T>(size)
        }

suspend fun <T : Any?> tryOrNullSuspended(
        call: suspend () -> T?
): T? {

    return try {
        call.invoke()
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

fun <T : Any?> tryOrNull(
        call: () -> T?
): T? {

    return try {
        call.invoke()
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

/**
 * Creates a suspend function
 * @param call - the body of the lambda
 *
 * @sample
 *      fun getNumber() = 2
 *      suspend fun getSuspendNumber{ getNumber() }
 *
 *      Both equal 2.
 */
suspend fun <T: Any> createSuspend(
        call: () -> T?
): T?{

    return suspendCoroutine { cont ->
        cont.resume(call())
    }
}


