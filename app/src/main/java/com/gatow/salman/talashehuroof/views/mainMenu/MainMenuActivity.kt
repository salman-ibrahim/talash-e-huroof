

package com.gatow.salman.talashehuroof.views.mainMenu

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.views.mainMenu.fragments.MainMenuFragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


/**
 * First Activity of the app and it's shows the menus for start the game
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class MainMenuActivity : AppCompatActivity() {


    var level: Int = 1
    var difficulty = "easy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.menuFragmentContainer,
                MainMenuFragment()
            )
            .commit()
    }

    fun replaceFragment(view: View, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addSharedElement(view, view.transitionName)
            .replace(R.id.menuFragmentContainer, fragment)
            .addToBackStack(view.transitionName)
            .commit()
    }

    /**
     * Hide system navigation and other UI to make game full screen
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}
