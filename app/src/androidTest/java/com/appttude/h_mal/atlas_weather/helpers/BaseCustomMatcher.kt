package com.appttude.h_mal.atlas_weather.helpers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

open class BaseCustomMatcher<T: Any> : TypeSafeMatcher<T>() {
    override fun describeTo(description: Description?) = describe(description)
    override fun matchesSafely(item: T): Boolean = match(item)

    open fun describe(description: Description?) { }
    open fun match(actual: T): Boolean { return false }
}