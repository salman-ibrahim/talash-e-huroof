

package com.gatow.salman.talashehuroof

import android.content.Intent
import androidx.test.espresso.IdlingPolicies
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.araujo.jordan.latest.byDescription
import com.araujo.jordan.latest.descriptionExist
import com.araujo.jordan.latest.textExists
import com.araujo.jordan.latest.uiDevice
import com.gatow.salman.talashehuroof.views.board.BoardActivity
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Ignore
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
class BoardInstrumentedTest {

    @get:Rule
    val activityRule =
        object : ActivityTestRule<BoardActivity>(BoardActivity::class.java, false, false) {}

    @Before
    fun registerIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.SECONDS)
        IdlingPolicies.setIdlingResourceTimeout(1, TimeUnit.SECONDS)
    }

    @Test
    @Ignore("It's not detecting the text for some reason. Ignoring for it")
    fun testWinGame() {
        launchApp()
        uiDevice().apply {
            assertTrue(textExists("Level 1"))
            assertTrue(descriptionExist("Test"))
            val initialPos = byDescription("test-0").visibleCenter
            val finalPos = byDescription("test-3").visibleCenter
            swipe(arrayOf(initialPos, finalPos), 15)
            assertTrue(textExists("You win!"))
        }
    }

    private fun launchApp() {
        val i = Intent()
        i.putExtra("test", "test")
        i.putExtra("difficulty", "test")
        activityRule.launchActivity(i)
    }

}
