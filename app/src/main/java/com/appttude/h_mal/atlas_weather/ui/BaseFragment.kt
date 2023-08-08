package com.appttude.h_mal.monoWeather.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.utils.Event
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.hide
import com.appttude.h_mal.atlas_weather.utils.show
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.properties.Delegates

@Suppress("EmptyMethod", "EmptyMethod")
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId),
    KodeinAware {

    override val kodein by kodein()
    val factory by instance<ApplicationViewModelFactory>()

    inline fun <reified VM : ViewModel> getFragmentViewModel(): Lazy<VM> = viewModels { factory }

    private var shortAnimationDuration by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    // toggle visibility of progress spinner while async operations are taking place
    fun progressBarStateObserver(progressBar: View) = Observer<Event<Boolean>> {
        it.getContentIfNotHandled()?.let { i ->
            if (i)
                progressBar.fadeIn()
            else
                progressBar.fadeOut()
        }
    }

    // display a toast when operation fails
    fun errorObserver() = Observer<Event<String>> {
        it.getContentIfNotHandled()?.let { message ->
            displayToast(message)
        }
    }

    fun refreshObserver(refresher: SwipeRefreshLayout) = Observer<Event<Boolean>> {
        refresher.isRefreshing = false
    }

    /**
     * Request a permission for
     * @param permission with
     * @param permissionCode
     * Callback if is already permission granted
     * @param permissionGranted
     */
    fun getPermissionResult(
        permission: String,
        permissionCode: Int,
        permissionGranted: () -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), permissionCode)
            return
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                permissionGranted.invoke()
            }
        }
    }

    private fun View.fadeIn() {
        apply {
            // Set the content view to 0% opacity but visible, so that it is visible
            // (but fully transparent) during the animation.
            alpha = 0f
            hide()

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        show()
                    }
                })
        }
    }

    private fun View.fadeOut() {
        apply {
            // Set the content view to 0% opacity but visible, so that it is visible
            // (but fully transparent) during the animation.
            alpha = 1f
            show()

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        hide()
                    }
                })
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                permissionsGranted()
                displayToast("Permission granted")
            } else {
                permissionsRefused()
                displayToast("Permission denied")
            }
        }
    }

    open fun permissionsGranted() {}
    open fun permissionsRefused() {}
}