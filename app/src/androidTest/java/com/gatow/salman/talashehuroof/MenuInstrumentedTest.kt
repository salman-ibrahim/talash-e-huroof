

package com.gatow.salman.talashehuroof

import android.content.Intent
import androidx.test.espresso.IdlingPolicies
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.araujo.jordan.latest.scrollUntilFindText
import com.araujo.jordan.latest.textClick
import com.araujo.jordan.latest.textExists
import com.araujo.jordan.latest.uiDevice
import com.gatow.salman.talashehuroof.models.Player
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder
import com.gatow.salman.talashehuroof.presenter.storage.StorageUtils
import com.gatow.salman.talashehuroof.views.mainMenu.MainMenuActivity
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Instrumented tests for the game main menu
 * It will navigate for all the options of menu of the game
 * @author Salman Ibrahim (salmanmaik-pk)
 */
@LargeTest
@RunWith(androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner::class)
class MenuInstrumentedTest {

    @get:Rule
    val activityRule =
        object : ActivityTestRule<MainMenuActivity>(MainMenuActivity::class.java, false, false) {}

    @Before
    fun registerIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.SECONDS)
        IdlingPolicies.setIdlingResourceTimeout(1, TimeUnit.SECONDS)
    }

    @Test
    fun testMenuAbout() {
        launchApp()
        uiDevice().apply {
            textClick("About")
            assertTrue(scrollUntilFindText("Contact me")?.isEnabled == true)
        }
    }

    @Test
    fun testMenuExit() {
        launchApp()
        uiDevice().apply {
            textClick("Exit")
            assertFalse(textExists("Play"))
        }
    }

    @Test
    fun testMenuDifficulty() {
        launchApp()
        uiDevice().apply {
            textClick("Play")
            assertTrue(textExists("Easy"))
            assertTrue(textExists("Medium"))
            assertTrue(textExists("Hard"))
        }
    }

    @Test
    fun testMenuLevels() {
        launchApp()
        uiDevice().apply {
            StorageUtils().savePlayer(activityRule.activity,
                Player(12)
            )
            textClick("Play")
            textClick("Easy")
            repeat(LevelBuilder().getLevels().size) {
                assertTrue(textExists("${it + 1}"))
            }
        }
    }

    @Test
    fun testAccessAllLevelsAndDifficulties() {
        launchApp()
        uiDevice().apply {
            StorageUtils().savePlayer(activityRule.activity,
                Player(12)
            )
            textClick("Play")
            arrayOf("Easy", "Medium", "Hard").forEach { difficulty ->
                textClick(difficulty)
                repeat(
                    LevelBuilder().getLevels().size) {
                    textClick("${it + 1}")
                    assertTrue(textExists("Level ${it + 1}", 8000))
                    pressBack()
                }
                pressBack()
            }
        }
    }

    @Test
    fun testMenuLevels5Enabled() {
        launchApp()
        uiDevice().apply {
            StorageUtils().savePlayer(activityRule.activity,
                Player(5)
            )
            textClick("Play")
            textClick("Easy")
            textClick("6")
            assertFalse(textExists("Level 6"))
            textClick("5")
            assertTrue(textExists("Level 5"))
        }
    }

    private fun launchApp() {
        val i = Intent()
        activityRule.launchActivity(i)
    }

}
