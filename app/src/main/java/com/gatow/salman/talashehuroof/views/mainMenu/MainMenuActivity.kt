

package com.gatow.salman.talashehuroof.views.mainMenu

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.views.mainMenu.fragments.MainMenuFragment

/**
 * First Activity of the app and it's shows the menus for start the game
 * @author Jordan L. Araujo Jr. (araujojordan)
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

}
