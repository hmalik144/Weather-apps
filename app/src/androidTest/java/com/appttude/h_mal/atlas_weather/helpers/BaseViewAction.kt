package com.appttude.h_mal.atlas_weather.helpers

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

open class BaseViewAction: ViewAction {
    override fun getDescription(): String? = setDescription()

    override fun getConstraints(): Matcher<View> = setConstraints()

    override fun perform(uiController: UiController?, view: View?) {
        setPerform(uiController, view)
    }

    open fun setDescription(): String? {
        return null
    }

    open fun setConstraints(): Matcher<View> {
        return isAssignableFrom(View::class.java)
    }

    open fun setPerform(uiController: UiController?, view: View?) { }
}