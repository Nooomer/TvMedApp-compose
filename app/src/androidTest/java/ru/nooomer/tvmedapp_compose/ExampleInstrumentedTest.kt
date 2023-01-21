package ru.nooomer.tvmedapp_compose

import android.content.Intent
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.nooomer.tvmedapp_compose", appContext.packageName)
    }
    @Test
    fun checkButtonDisabled() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals("ru.nooomer.tvmedapp_compose", appContext.packageName)
    }
}
@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    private lateinit var scenario: ActivityScenario<MainActivity>
    @JvmField
    @Rule
    val rule = activityScenarioRule<MainActivity>()
    @Test
    fun someTest() {
        scenario = rule.scenario
        scenario.onActivity {

        }
    }
    @After
    fun cleanup() {
        scenario.close()
    }
}
