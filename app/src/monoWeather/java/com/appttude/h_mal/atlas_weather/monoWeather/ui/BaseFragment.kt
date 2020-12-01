package com.appttude.h_mal.atlas_weather.monoWeather.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.appttude.h_mal.atlas_weather.utils.Event
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.hide
import com.appttude.h_mal.atlas_weather.utils.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment(){

    // toggle visibility of progress spinner while async operations are taking place
    fun progressBarStateObserver(progressBar: View) = Observer<Event<Boolean>> {
        when(it.getContentIfNotHandled()){
            true -> {
                progressBar.show()
            }
            false -> {
                progressBar.hide()
            }
        }
    }

    // display a toast when operation fails
    fun errorObserver() = Observer<Event<String>> {
        it.getContentIfNotHandled()?.let { message ->
            displayToast(message)
        }
    }

    @SuppressLint("MissingPermission")
    fun getPermissionResult(
            permission: String,
            permissionCode: Int,
            permissionGranted: () -> Unit
    ){
        if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), permissionCode)
            return
        }else{
            CoroutineScope(Dispatchers.Main).launch{
                permissionGranted.invoke()
            }
        }

    }

}