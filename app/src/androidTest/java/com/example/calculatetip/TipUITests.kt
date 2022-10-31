package com.example.calculatetip

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.calculatetip.ui.theme.CalculateTipTheme
import com.example.tipcalculator.TipTimeScreen
import org.junit.Rule
import org.junit.Test

class TipUITests {
    @get:Rule
    val composeTestRule = createComposeRule()
    //Compose tests -> instrumentation tests olarak da gecer.
    //activity, fragment gibi android isletim sistemine bagli olan componentleri test etmemizi saglar sağlar.
    //espresso ile uyumlu calisir.

    @Test
    fun calculate_20_percent_tip(){
        composeTestRule.setContent {
            CalculateTipTheme {
                TipTimeScreen()
            }
        }

        composeTestRule.onNodeWithText("Cost of Service")
            .performTextInput("10")
        composeTestRule.onNodeWithText("Tip (%)")
            .performTextInput("20")
        composeTestRule.onNodeWithText("Tip Amount: $2.00").assertExists()

    }


}