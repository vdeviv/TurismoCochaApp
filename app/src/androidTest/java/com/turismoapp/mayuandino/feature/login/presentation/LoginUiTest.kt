package com.turismoapp.mayuandino.feature.login.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class LoginUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificar_campos_y_boton_login() {

        composeTestRule.setContent {
            LoginScreen(onSuccess = {})
        }

        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()
            .performTextInput("usuario@mayuandino.com")

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()
            .performTextInput("Password123")

        composeTestRule.onNodeWithText("Iniciar Sesión")
            .assertExists()
            .performClick()
    }
}