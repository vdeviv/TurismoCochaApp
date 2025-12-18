package com.turismoapp.mayuandino.feature.maintenance.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class MaintenanceOverlayTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificar_mensaje_de_mantenimiento_visible() {
        composeTestRule.setContent {
            MaintenanceOverlay()
        }
        composeTestRule.onNodeWithText("Estamos mejorando Mayu Andino")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Volveremos pronto con nuevas rutas, eventos y experiencias.")
            .assertIsDisplayed()
    }
}