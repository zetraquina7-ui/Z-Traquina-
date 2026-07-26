package com.example

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ui.navigation.Screen
import com.example.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppCrashTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNavigateToChatScreen() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val mainViewModel = MainViewModel(app)
        
        composeTestRule.setContent {
            MainAppContent(viewModel = mainViewModel)
        }
        
        // Navigate to Chat
        composeTestRule.runOnUiThread {
            mainViewModel.navigateTo(Screen.Chat)
        }
        
        composeTestRule.waitForIdle()
        
        // Assert chat screen is visible
        composeTestRule.onNodeWithTag("chat_screen").assertExists()
    }
}
