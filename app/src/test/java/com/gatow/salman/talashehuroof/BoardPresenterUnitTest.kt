

package com.gatow.salman.talashehuroof


import android.os.Build
import com.gatow.salman.talashehuroof.models.BoardCharacter
import com.gatow.salman.talashehuroof.models.WordAvailable
import com.gatow.salman.talashehuroof.presenter.board.BoardListener
import com.gatow.salman.talashehuroof.presenter.board.BoardPresenter
import com.gatow.salman.talashehuroof.utils.KTestWait
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

/**
 * Unit Test for the board generation
 * @author Salman Ibrahim (salmanmaik-pk)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class BoardPresenterUnitTest {

    @Test
    @DisplayName("Check for win situation")
    fun checkForVictory() {
        val waitTest = KTestWait<Boolean>(3000)
        val boardPresenter: BoardPresenter?
        boardPresenter =
            BoardPresenter(object :
                BoardListener {
                override fun onVictory() {
                    waitTest.send(true)
                }

                override fun onLose() {
                    waitTest.send(false)
                }

                override fun updateWordList(words: ArrayList<WordAvailable>) {
                    println("updateWordList() words:$words")
                }

                override fun updateSelectedWord(
                    selectingWord: ArrayList<BoardCharacter>?,
                    acceptedWord: Boolean
                ) {
                    println("updateSelectedWord:$selectingWord")
                    println("acceptedWord:$acceptedWord")
                }

                override fun updateClockTime(clock: String) {
                    println("clock:$clock")
                }
            })

        boardPresenter.buildEmptyBoard()
        boardPresenter.reset("test")
        boardPresenter.startCounter("test")
        boardPresenter.addCharacter(boardPresenter.board[0][0])
        boardPresenter.addCharacter(boardPresenter.board[1][0])
        boardPresenter.addCharacter(boardPresenter.board[2][0])
        boardPresenter.addCharacter(boardPresenter.board[3][0])
        boardPresenter.checkForWord()
        assertTrue(waitTest.receive() == true)
    }

    @Test
    @DisplayName("Check for lose situation")
    fun checkForLose() {
        val waitTest = KTestWait<Boolean>(6000)
        val boardPresenter: BoardPresenter?
        boardPresenter =
            BoardPresenter(object :
                BoardListener {
                override fun onVictory() {
                    waitTest.send(true)
                }

                override fun onLose() {
                    println("Time out!")
                    waitTest.send(false)
                }

                override fun updateWordList(words: ArrayList<WordAvailable>) {}
                override fun updateSelectedWord(
                    selectingWord: ArrayList<BoardCharacter>?,
                    acceptedWord: Boolean
                ) {
                }

                override fun updateClockTime(clock: String) {
                    println("clock:$clock")
                }
            })

        boardPresenter.buildEmptyBoard()
        boardPresenter.reset("test")
        val shadowTimer = Shadows.shadowOf(boardPresenter.startCounter("test"))
        boardPresenter.addCharacter(boardPresenter.board[0][0])
        boardPresenter.addCharacter(boardPresenter.board[1][0])
        boardPresenter.addCharacter(boardPresenter.board[2][0])
        boardPresenter.checkForWord()
        shadowTimer.invokeFinish()
        assertTrue(waitTest.receive() == false)
    }


}
