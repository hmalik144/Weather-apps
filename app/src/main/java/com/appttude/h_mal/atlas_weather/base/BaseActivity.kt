package com.appttude.h_mal.atlas_weather.base

import android.content.Intent
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.inflate
import androidx.appcompat.app.AppCompatActivity
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.utils.hide
import com.appttude.h_mal.atlas_weather.utils.show
import com.appttude.h_mal.atlas_weather.utils.triggerAnimation
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    private var loadingView: View? = null

    override val kodein by kodein()


    /**
     *  Creates a loading view which to be shown during async operations
     *
     *  #setOnClickListener(null) is an ugly work around to prevent under being clicked during
     *  loading
     */
    private fun instantiateLoadingView() {
        loadingView = inflate(this, R.layout.progress_layout, null)
        loadingView?.setOnClickListener(null)
        addContentView(loadingView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        loadingView?.hide()
    }

    override fun onStart() {
        super.onStart()
        instantiateLoadingView()
    }

    fun <A : AppCompatActivity> startActivity(activity: Class<A>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    /**
     *  Called in case of success or some data emitted from the liveData in viewModel
     */
    open fun onStarted() {
        loadingView?.fadeIn()
    }

    /**
     *  Called in case of success or some data emitted from the liveData in viewModel
     */
    open fun onSuccess(data: Any?) {
        loadingView?.fadeOut()
    }

    /**
     *  Called in case of failure or some error emitted from the liveData in viewModel
     */
    open fun onFailure(error: Any?) {
        if (error is String) displayToast(error)
        loadingView?.fadeOut()
    }

    private fun View.fadeIn() = apply {
        show()
        triggerAnimation(R.anim.nav_default_enter_anim) {}
    }

    private fun View.fadeOut() = apply {
        hide()
        triggerAnimation(R.anim.nav_default_exit_anim) {}
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        loadingView?.hide()
        super.onBackPressed()
    }
}