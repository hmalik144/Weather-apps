package com.appttude.h_mal.atlas_weather.helpers

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import tools.fastlane.screengrab.Screengrab

/**
 * Junit rule that takes a screenshot when a test fails.
 */
class SnapshotRule : TestWatcher() {
    override fun failed(e: Throwable, description: Description) {
        // Catch a screenshot on failure
        Screengrab.screenshot("FAILURE-" + getScreenshotName(description))
    }

    fun getScreenshotName(description: Description): String {
        return description.className.replace(".", "-") + "_" + description.methodName.replace(
            ".",
            "-"
        )
    }
}