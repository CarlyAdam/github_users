package com.carlyadam.github

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.facebook.testing.screenshot.Screenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScreenshotTest {
    @get:Rule
    var activityTestRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, false, false)

    @Before
    fun before() {
        InstrumentationRegistry.getInstrumentation().uiAutomation
    }

    @Test
    fun testScreenshotEntireActivity() {
        val activity = activityTestRule.launchActivity(null)
        Screenshot.snapActivity(activity).record()
    }
}
