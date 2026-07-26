package com.example.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class AppCrashTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppDoesNotCrash() {
        composeTestRule.waitForIdle()
        try {
            composeTestRule.onNodeWithTag("home_quick_chat").performClick()
            composeTestRule.waitForIdle()
            println("AFTER CLICKING ZÉAI:")
            println(composeTestRule.onRoot().printToString())
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
