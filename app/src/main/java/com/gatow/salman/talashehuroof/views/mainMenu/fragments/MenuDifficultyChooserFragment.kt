

package com.gatow.salman.talashehuroof.views.mainMenu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.views.mainMenu.MainMenuActivity
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_difficulty_chooser.*

/**
 * Fragment that show the difficulty menu in the MainMenuActivity
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class MenuDifficultyChooserFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?) =
        inflater.inflate(R.layout.fragment_difficulty_chooser, view, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goBackButton.setOnClickListener { activity?.onBackPressed() }

        menuLevel1.setOnClickListener {
            val act = (activity as? MainMenuActivity)
            act?.difficulty = "easy"
            act?.replaceFragment(view, MenuLevelSelectionFragment())
        }
        menuDifficultyMedium.setOnClickListener {
            val act = (activity as? MainMenuActivity)
            act?.difficulty = "medium"
            act?.replaceFragment(view, MenuLevelSelectionFragment())
        }
        menuDifficultyHard.setOnClickListener {
            val act = (activity as? MainMenuActivity)
            act?.difficulty = "hard"
            act?.replaceFragment(view, MenuLevelSelectionFragment())
        }


    }

}