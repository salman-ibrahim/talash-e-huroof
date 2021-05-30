

package com.gatow.salman.talashehuroof.presenter.storage

import android.annotation.SuppressLint
import android.content.Context
import com.gatow.salman.talashehuroof.BuildConfig.APPLICATION_ID
import com.gatow.salman.talashehuroof.models.Player
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder

/**
 * Used to preserve game status across app restarts
 * @author Jordan L. Araujo Jr. (araujojordan)
 */
class StorageUtils {

    private val wordFinderAlias = APPLICATION_ID

    /**
     * Get player information from SharedPreferences
     * @param context used to access SharedPref
     * @return the player of the game
     */
    fun getPlayer(ctx: Context): Player {
        val sharedPreferences = ctx.getSharedPreferences(wordFinderAlias, Context.MODE_PRIVATE)
        return Player(
            level = sharedPreferences.getInt("level", 1)
        )
    }

    /**
     * Save the player information on SharedPreferences
     * @param context used to access SharedPref
     * @param player player to be saved
     */
    fun savePlayer(ctx: Context, player: Player) {
        require(player.level in 0..LevelBuilder()
            .getLevels().size)
        val sharedPreferences = ctx.getSharedPreferences(wordFinderAlias, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("level", player.level)
        editor.apply()
    }

    /**
     * Full delete game saved preferences
     */
    @SuppressLint("ApplySharedPref")
    fun deleteData(ctx: Context) {
        val sharedPreferences = ctx.getSharedPreferences(wordFinderAlias, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()
    }
}