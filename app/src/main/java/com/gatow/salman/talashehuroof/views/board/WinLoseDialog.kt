

package com.gatow.salman.talashehuroof.views.board

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder
import kotlinx.android.synthetic.main.dialog_game_end.view.*

/**
 * Dialog that show the win, lose or error messages
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class WinLoseDialog(act: BoardActivity, victory: Boolean, msg: String? = null) {
    init {
        val dialog = AlertDialog.Builder(act, R.style.RoundedDialog)
        dialog.setCancelable(false)
        val v = act.layoutInflater.inflate(R.layout.dialog_game_end, null)
        dialog.setView(v)
        val dialogComplete = dialog.show()
        v.dialogWinLoseLogo.startAnimation(AnimationUtils.loadAnimation(act, R.anim.winlose_anim))
        v.dialogWinLoseLogo.setImageDrawable(
            AppCompatResources.getDrawable(
                v.context,
                if (victory) R.drawable.ic_win else R.drawable.ic_gameover
            )
        )
        if (msg != null) {
            v.dialogWinloseText.text = msg
        } else
            v.dialogWinloseText.setText(if (victory) R.string.dialogYouWinTitle else R.string.loseTitleDialog)
        v.dialogRightButtonImage.setImageDrawable(
            AppCompatResources.getDrawable(
                v.context,
                if (victory) R.drawable.ic_play_arrow_black_24dp else R.drawable.ic_levels
            )
        )
        val level = act.intent.getIntExtra("level", 1)
        if (victory && level == LevelBuilder()
                .getLevels().size) v.dialogRightButton.visibility =
            View.GONE
        v.dialogRightButton.setOnClickListener {
            act.finish()
            if (victory) {
                val nextLevel = Intent(act, BoardActivity::class.java)
                nextLevel.putExtra("difficulty", act.intent.getStringExtra("difficulty"))
                nextLevel.putExtra("level", level)
                dialogComplete.dismiss()
            }
        }
        v.dialogLeftButton.setOnClickListener {
            act.reset()
            dialogComplete.dismiss()
        }
    }
}