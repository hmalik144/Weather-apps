package com.appttude.h_mal.atlas_weather.mvvm.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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

fun ImageView.loadImage(url: String?){
    Picasso.get()
            .load(url)
            .placeholder(R.mipmap.ic_launcher)
            .into(this)
}

fun ViewGroup.generateView(layoutId: Int): View = LayoutInflater
        .from(context)
        .inflate(layoutId, this, false)

fun ImageView.loadImage(url: String?, height: Int, width: Int){
    Picasso.get()
            .load(url)
            .resize(width, height)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(this)
}

fun SearchView.onSubmitListener(searchSubmit: (String) -> Unit) {
    this.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(s: String): Boolean {
            searchSubmit.invoke(s)
            return true
        }

        override fun onQueryTextChange(s: String): Boolean {
            return true
        }
    })
}