package com.turismoapp.mayuandino.feature.home.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificar_titulo_principal_home() {
        composeTestRule.setContent {
            HomeScreen(onProfileClick = {}, onNotificationClick = {}, onPlaceClick = {})
        }

        composeTestRule.onNodeWithText("Cochabamba!")
            .assertIsDisplayed()
    }
}