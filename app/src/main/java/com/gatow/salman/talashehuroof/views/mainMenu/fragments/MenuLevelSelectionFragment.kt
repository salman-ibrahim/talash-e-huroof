

package com.gatow.salman.talashehuroof.views.mainMenu.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.views.board.BoardActivity
import com.gatow.salman.talashehuroof.views.mainMenu.MainMenuActivity
import com.araujojordan.ktlist.KtList
import com.gatow.salman.talashehuroof.models.Player
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder
import com.gatow.salman.talashehuroof.presenter.storage.StorageUtils
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_difficulty_chooser.goBackButton
import kotlinx.android.synthetic.main.fragment_level_chooser.*
import kotlinx.android.synthetic.main.item_level.view.*

/**
 * Fragment that show the level selection menu in the MainMenuActivity
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class MenuLevelSelectionFragment : Fragment() {

    var player: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?) =
        inflater.inflate(R.layout.fragment_level_chooser, view, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goBackButton.setOnClickListener { activity?.onBackPressed() }

        levelGrid.adapter = KtList(
            LevelBuilder().getLevels(),
            R.layout.item_level,
            clickListener = { level, _, _ ->
                if (player?.level ?: 1 >= level.level) {
                    val act = (activity as? MainMenuActivity)
                    startActivity(Intent(context, BoardActivity::class.java).apply {
                        putExtra("difficulty", act?.difficulty)
                        putExtra("level", level.level)
                    })
                }
            }
        ) { level, itemView ->
            itemView.alpha = if (player?.level ?: 1 >= level.level) 1.0f else 0.5f
            itemView.itemLevelNumber.text = (level.level.toString())
                .replace("0","۰")
                .replace("1","۱")
                .replace("2","۲")
                .replace("3","۳")
                .replace("4","۴")
                .replace("5","۵")
                .replace("6","۶")
                .replace("7","۷")
                .replace("8","۸")
                .replace("9","۹")
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            player = StorageUtils()
                .getPlayer(it)
            Log.d("onResume()", "getPlayer ${player?.level}")
            levelGrid?.adapter?.notifyDataSetChanged()
        }
    }
}