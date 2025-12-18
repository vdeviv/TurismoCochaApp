package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel
import org.junit.Rule
import org.junit.Test

class PackageCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificar_conteo_de_personas_unidas() {
        val mockPackage = PackageModel(
            title = "Paquete Prueba",
            joinedUsers = listOf("user1", "user2", "user3")
        )

        composeTestRule.setContent {
            PackageCard(pkg = mockPackage, onClick = {})
        }

        composeTestRule.onNodeWithText("3 personas se unieron")
            .assertIsDisplayed()
    }
}