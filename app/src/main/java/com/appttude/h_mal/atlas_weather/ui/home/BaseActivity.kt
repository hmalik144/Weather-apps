package com.appttude.h_mal.atlas_weather.ui.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseActivity : AppCompatActivity(){

    @SuppressLint("MissingPermission")
    fun getPermissionResult(
            permission: String,
            permissionCode: Int,
            permissionGranted: () -> Unit
    ){
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), permissionCode)
            return
        }else{
            CoroutineScope(Dispatchers.Main).launch{
                permissionGranted.invoke()
            }
        }

    }

}