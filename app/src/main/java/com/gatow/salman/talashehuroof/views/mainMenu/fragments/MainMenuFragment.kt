

package com.gatow.salman.talashehuroof.views.mainMenu.fragments

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.views.mainMenu.MainMenuActivity
import kotlinx.android.synthetic.main.fragment_main_menu.*

/**
 * Fragment that show first menu in the MainMenuActivity
 * @author Jordan L. Araujo Jr. (araujojordan)
 */
class MainMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?) =
        inflater.inflate(R.layout.fragment_main_menu, view, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainMenuLogo.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.fade_in__logo_appear)
        )


        mainMenuPlayButton.setOnClickListener {
            (activity as? MainMenuActivity)?.replaceFragment(
                it,
                MenuDifficultyChooserFragment()
            )

        }

        mainMenuAboutButton.setOnClickListener {
            (activity as? MainMenuActivity)?.replaceFragment(
                it,
                MenuAboutFragment()
            )
        }
        mainMenuExitButton.setOnClickListener { activity?.finish() }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
        ) {
            try {
                val ft = fragmentManager?.beginTransaction()
                if (Build.VERSION.SDK_INT >= 26) ft?.setReorderingAllowed(false)
                ft?.detach(this)?.attach(this)?.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}