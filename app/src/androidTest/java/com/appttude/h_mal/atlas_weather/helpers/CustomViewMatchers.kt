package com.appttude.h_mal.atlas_weather.helpers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * Matcher for testing error of TextInputLayout
 */
fun checkErrorMessage(expectedErrorText: String): Matcher<View?> {
    return object : TypeSafeMatcher<View?>() {
        override fun matchesSafely(view: View?): Boolean {
            if (view is EditText) {
                return view.error.toString() == expectedErrorText
            }

            if (view !is TextInputLayout) return false

            val error = view.error ?: return false
            return expectedErrorText == error.toString()
        }

        override fun describeTo(d: Description?) {}
    }
}

fun checkImage(): Matcher<View?> {
    return object : TypeSafeMatcher<View?>() {
        override fun matchesSafely(view: View?): Boolean {
            if (view is ImageView) {
                return hasImage(view)
            }
            return false
        }

        override fun describeTo(d: Description?) {}

        private fun hasImage(view: ImageView): Boolean {
            val drawable = view.drawable
            var hasImage = drawable != null
            if (hasImage && drawable is BitmapDrawable) {
                hasImage = drawable.bitmap != null
            }
            return hasImage
        }
    }
}

