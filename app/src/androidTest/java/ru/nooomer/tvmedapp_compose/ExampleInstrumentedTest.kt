package ru.nooomer.tvmedapp_compose

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    @JvmField
    @Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun checkDisabledButtonWoutText() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Войти").assertIsNotEnabled()
    }
    @Test
    fun checkDisabledButtonOnlyWithPhoneText() {
        composeTestRule.waitForIdle()
        getTextFieldNodeAndSetText("Номер телефона", "89534500038")
        composeTestRule.onNodeWithText("Войти").assertIsNotEnabled()
    }
    @Test
    fun checkDisabledButtonOnlyWithPasswordText() {
        composeTestRule.waitForIdle()
        getTextFieldNodeAndSetText("Пароль", "1234")
        composeTestRule.onNodeWithText("Войти").assertIsNotEnabled()
    }
    @Test
    fun checkEnabledButtonWithPasswordAndPhoneText() {
        composeTestRule.waitForIdle()
        getTextFieldNodeAndSetText("Номер телефона", "89534500038")
        getTextFieldNodeAndSetText("Пароль", "1234")
        composeTestRule.onNodeWithText("Войти").assertIsEnabled()
    }
    @Test
    fun checkOutlinedLowPhoneNumber() {
        composeTestRule.waitForIdle()
        getTextFieldNodeAndSetText("Номер телефона", "1234")
        composeTestRule.onNodeWithText("Пароль").performClick()
        composeTestRule.onRoot().printToLog("TAG")
        val node = composeTestRule.onNode(SemanticsMatcher.expectValue(phoneErrorSemanticsKey, true)).printToLog("TAG")
    }

    private fun getTextFieldNodeAndSetText(nodeName: String, text: String){
        val textFiledNode = composeTestRule.onNodeWithText(nodeName, useUnmergedTree = false)
        textFiledNode.performClick()
        textFiledNode.performTextInput(text)
    }
}
