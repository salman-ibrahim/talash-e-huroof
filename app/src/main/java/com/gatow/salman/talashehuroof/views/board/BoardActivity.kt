

package com.gatow.salman.talashehuroof.views.board

import android.bluetooth.le.AdvertiseData
import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.araujojordan.ktlist.KtList
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.models.BoardCharacter
import com.gatow.salman.talashehuroof.models.WordAvailable
import com.gatow.salman.talashehuroof.presenter.board.BoardListener
import com.gatow.salman.talashehuroof.presenter.board.BoardPresenter
import com.gatow.salman.talashehuroof.presenter.level.LevelBuilder
import com.gatow.salman.talashehuroof.presenter.storage.StorageUtils
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.board_activity.*
import kotlinx.android.synthetic.main.item_words.view.*
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main Activity of the Game Board
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class BoardActivity : AppCompatActivity(),
    BoardListener {

    var boardPresenter =
        BoardPresenter(this)
    private var boardAdapter: BoardAdapter? = null
    private val wordsAvailableAdapter by lazy {
        KtList(boardPresenter.allWords, R.layout.item_words) { word, itemView ->
            val textView = itemView.itemText
            val itemStrikethough = itemView.itemStrikethough

            textView.text = word.word
            textView.contentDescription = word.word

            if (word.strikethrough) {
                if (!word.didAnimation) {
                    val anim = ScaleAnimation(
                        0.0f, 1.0f, 1.0f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    anim.duration = 1000
                    itemStrikethough.startAnimation(anim)
                    word.didAnimation = true
                }
                itemStrikethough.alpha = 1.0f
                itemView.alpha = 0.5f
            } else {
                itemStrikethough.alpha = 0.0f
                itemView.alpha = 1f
            }
        }
    }

    //Necessary for rotation
    private var activityBoardSelectedWord: TextView? = null
    private var viewKonfetti: KonfettiView? = null
    private var activityBoardGrid: RecyclerView? = null
    private var activityBoardAvailableWordsGrid: RecyclerView? = null
    private var activityBoardResetButton: ImageView? = null
    private var activityBoardTimer: TextView? = null
    lateinit var mAdView : AdView
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "GameOver"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_activity)

        // In-game Banner ad initialize here
        MobileAds.initialize(this) {
            mAdView = findViewById(R.id.inGameAd)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }

        //Interstitial Ad
        val adRequest = AdRequest.Builder().build()
        val adID = this.resources.getString(R.string.gameOverInterstitial)

        InterstitialAd.load(this,adID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }


        })

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        boardLevelLabel.text = getString(
            R.string.levelLabel,
            intent.getIntExtra("level", 1)
        ).replace("0","۰")
            .replace("1","۱")
            .replace("2","۲")
            .replace("3","۳")
            .replace("4","۴")
            .replace("5","۵")
            .replace("6","۶")
            .replace("7","۷")
            .replace("8","۸")
            .replace("9","۹")

        boardAdapter = BoardAdapter(
            this,
            boardPresenter
        )
        linkBoard()
        reset()
    }

    override fun onDestroy() {
        super.onDestroy()

        //Show interstitial
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }

        //nulling views reference to avoid memory leak
        activityBoardSelectedWord = null
        viewKonfetti = null
        activityBoardGrid = null
        activityBoardAvailableWordsGrid = null
        activityBoardResetButton = null
        activityBoardTimer = null
        boardPresenter.onDestroy()
    }

    override fun updateSelectedWord(
        selectingWord: ArrayList<BoardCharacter>?,
        acceptedWord: Boolean
    ) {

        if (selectingWord?.size ?: 20 > 10) {
            activityBoardSelectedWord?.text = ""
            return
        }

        Log.d("BoardActivity", "selectingWord()")
        if (acceptedWord) {
            activityBoardSelectedWord?.animate()?.setInterpolator(DecelerateInterpolator())
                ?.scaleX(2f)
                ?.scaleY(2f)?.alpha(0f)?.withEndAction {
                    activityBoardSelectedWord?.text = ""
                    activityBoardSelectedWord?.alpha = 1f
                    activityBoardSelectedWord?.scaleX = 1f
                    activityBoardSelectedWord?.scaleY = 1f
                }?.start()
            return
        } else {
            var word = ""
            selectingWord?.forEach { word += it.char }
            activityBoardSelectedWord?.text = word
        }
    }

    override fun onVictory() {

        val storage =
            StorageUtils()
        val player = storage.getPlayer(this)
        if (player.level < LevelBuilder()
                .getLevels().size) {
            ++player.level
            storage.savePlayer(this, player)

        }

        // Fix the level error
//        if (!LevelBuilder()
//                .getLevelStatus(player.level)) {
//            LevelBuilder().setLevelStatus(player.level)
//            ++player.level
//            storage.savePlayer(this, player)
//        }


        viewKonfetti?.build()?.apply {
            addColors(Color.YELLOW, Color.BLUE, Color.MAGENTA)
            setDirection(0.0, 359.0)
            setSpeed(1f, 5f)
            setFadeOutEnabled(true)
            setTimeToLive(1500L)
            addShapes(Shape.Square, Shape.Circle)
            addSizes(Size(12))
            setPosition(-50f, (viewKonfetti?.width ?: 0) + 50f, -50f, -50f)
            streamFor(300, 1500L)
            showDialog(true)
        }
    }

    /**
     * Hide system navigation to make game full screen
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

    /**
     * Automatically show GameOver screen
     */
    override fun onLose() {
        showDialog(false)
    }

    /**
     * Update the bottom clock time
     */
    override fun updateClockTime(clock: String) {
        activityBoardTimer?.text = clock
    }

    /**
     * Reset game and timeout.
     * This will random the words in the board and
     * it will also get another random word list if it's not fixed.
     */
    fun reset() {
        boardLoading?.visibility = View.VISIBLE
        Thread {
            try {
                boardPresenter.reset(getLevel())
                updateBoard()
            } catch (err: Exception) {
                showError(err.message.toString())
            }
        }.start()
    }

    private fun getLevel(): String {
        if (intent.hasExtra("test"))
            return "test"
        return LevelBuilder().getCategory(
            intent.getIntExtra("level", 1)
        )
    }

    private fun showError(errorMessage: String) = runOnUiThread {
        when {
            errorMessage.contains("Failed to execute http call", true) ||
                    errorMessage.contains("Unable to resolve host", true) ||
                    errorMessage.contains("connect ETIMEDOUT", true) ||
                    errorMessage.contains("HTTP 503", true) ->
                showDialog(false, "No internet connection!")
            else -> showDialog(false, errorMessage)
        }
    }

    private fun updateBoard() = runOnUiThread {
        wordsAvailableAdapter.setList(boardPresenter.allWords)
        boardAdapter?.updateGrid(boardPresenter.board)

        boardPresenter.startCounter(getDifficulty())
        boardLoading?.visibility = View.GONE
        activityBoardAvailableWordsGrid?.visibility = View.VISIBLE
    }

    private fun getDifficulty(): String {
        if (intent.hasExtra("difficulty"))
            return intent.getStringExtra("difficulty") ?: "hard"
        return "hard"
    }

    private fun showDialog(victory: Boolean, msg: String? = null) {
        WinLoseDialog(this, victory, msg)
    }

    override fun updateWordList(words: ArrayList<WordAvailable>) {
        MediaPlayer.create(this, R.raw.levelup).apply {
            setOnPreparedListener {
                setVolume(1f, 1f)
                it.start()
            }
        }
        wordsAvailableAdapter.setList(words)
    }

    private fun linkBoard(localPresenter: BoardPresenter? = null) {

        //necessary linking after rotation
        activityBoardSelectedWord = findViewById(R.id.activityBoardSelectedWord)
        viewKonfetti = findViewById(R.id.viewKonfetti)
        activityBoardGrid = findViewById(R.id.activityBoardGrid)
        activityBoardAvailableWordsGrid = findViewById(R.id.activityBoardAvailableWordsGrid)
        activityBoardResetButton = findViewById(R.id.activityBoardResetButton)
        activityBoardTimer = findViewById(R.id.activityBoardTimer)

        activityBoardGrid?.adapter = boardAdapter
        (localPresenter ?: boardPresenter).board = boardPresenter.buildEmptyBoard()
        boardAdapter?.updateGrid((localPresenter ?: boardPresenter).board)
        activityBoardAvailableWordsGrid?.adapter = wordsAvailableAdapter
        activityBoardResetButton?.setOnClickListener {
            it.animate().rotation(360f).setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction { it.rotation = 0f }.start()
            reset()
        }

        activityBoardAvailableWordsGrid?.adapter?.notifyDataSetChanged()
        activityBoardGrid?.adapter?.notifyDataSetChanged()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val localPresenter = boardPresenter

        boardPresenter = localPresenter
        setContentView(R.layout.board_activity)
        boardPresenter = localPresenter

        linkBoard()
        boardPresenter = localPresenter
    }

}
