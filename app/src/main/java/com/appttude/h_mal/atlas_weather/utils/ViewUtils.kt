package com.appttude.h_mal.atlas_weather.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appttude.h_mal.atlas_weather.R
import com.bumptech.glide.Glide

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

fun ImageView.loadImage(url: String?){
    val c = Glide.with(this)
            .load(url)
    viewTreeObserver.addOnPreDrawListener {
        c.override(width, height)
        true
    }
    c.placeholder(R.drawable.ic_baseline_cloud_queue_24)
            .error(R.drawable.ic_baseline_cloud_off_24)
            .fitCenter()
            .into(this)
}

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}