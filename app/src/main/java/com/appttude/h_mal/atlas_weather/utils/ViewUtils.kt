package com.appttude.h_mal.atlas_weather.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.appttude.h_mal.atlas_weather.R
import com.squareup.picasso.Picasso

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.displayToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun ViewGroup.generateView(layoutId: Int): View = LayoutInflater
    .from(context)
    .inflate(layoutId, this, false)

fun ImageView.loadImage(url: String?) {
    Picasso.get().load(url)
        .placeholder(R.drawable.ic_baseline_cloud_queue_24)
        .error(R.drawable.ic_baseline_cloud_off_24)
        .into(this)
}

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun View.triggerAnimation(@AnimRes id: Int, complete: (View) -> Unit) {
    val animation = AnimationUtils.loadAnimation(context, id)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) = complete(this@triggerAnimation)
        override fun onAnimationStart(a: Animation?) {}
        override fun onAnimationRepeat(a: Animation?) {}
    })
    startAnimation(animation)
}