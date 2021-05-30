

package com.gatow.salman.talashehuroof

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.gatow.salman.talashehuroof.models.Player
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder
import com.gatow.salman.talashehuroof.presenter.storage.StorageUtils
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode


/**
 * Unit tests for the app storage
 * @author Salman Ibrahim (salmanmaik-pk)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class StorageUnitTest {

    @Test
    @DisplayName("Get Empty player")
    fun getEmptyPlayer() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val stUtils =
            StorageUtils()
        stUtils.deleteData(ctx)
        val player = stUtils.getPlayer(ctx)
        assertTrue(player.level == 1)
    }

    @Test
    @DisplayName("Get Stored player level 5")
    fun getPlayer() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val stUtils =
            StorageUtils()
        stUtils.deleteData(ctx)
        stUtils.savePlayer(ctx, Player(5))
        val player = stUtils.getPlayer(ctx)
        assertTrue(player.level == 5)

    }

    @Test
    @DisplayName("Check is player is being stored")
    fun savePlayer() {
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val stUtils =
            StorageUtils()
        stUtils.deleteData(ctx)

        stUtils.savePlayer(ctx, Player(5))
        val player = stUtils.getPlayer(ctx)
        assertTrue(player.level == 5)
    }

    @Test
    @DisplayName("Fail to save user with strange level")
    fun savePlayerFail() {

        val ctx = ApplicationProvider.getApplicationContext() as Context
        val stUtils =
            StorageUtils()

        Assertions.assertThrows(Exception::class.java) {
            stUtils.deleteData(ctx)
            stUtils.savePlayer(ctx,
                Player(-1)
            )
            assertTrue(true)
        }
        Assertions.assertThrows(Exception::class.java) {
            stUtils.deleteData(ctx)
            stUtils.savePlayer(ctx,
                Player(
                    LevelBuilder()
                        .getLevels().size + 1
                )
            )
            assertTrue(true)
        }
    }
}
