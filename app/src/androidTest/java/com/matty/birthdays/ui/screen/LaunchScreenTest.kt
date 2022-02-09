package com.matty.birthdays.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.matty.birthdays.ui.LaunchScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LaunchScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldCallHandlerExactlyOnceWhenDelayIsOver() {
        var handlerInvocationTimes = 0
        val onDelayFinished = { handlerInvocationTimes += 1 }

        composeRule.setContent {
            LaunchScreen(delayMillis = 0, onDelayFinished)
        }

        assertEquals(1, handlerInvocationTimes)
    }

    @Test
    fun shouldShowAppName() {
        composeRule.setContent {
            LaunchScreen(delayMillis = 0) {}
        }

        composeRule.onNodeWithTag("appName").assertIsDisplayed()
    }

    @Test
    fun shouldShowLogo() {
        composeRule.setContent {
            LaunchScreen(delayMillis = 0) {}
        }

        composeRule.onNodeWithTag("image").assertIsDisplayed()
    }
}