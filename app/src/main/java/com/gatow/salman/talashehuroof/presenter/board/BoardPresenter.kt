/**
 * Model class that represent the board
 *
 * @author Salman Ibrahim (salmanmaik-pk)
*/

package com.gatow.salman.talashehuroof.presenter.board

import android.os.CountDownTimer
import com.gatow.salman.talashehuroof.models.BoardCharacter
import com.gatow.salman.talashehuroof.models.WordAvailable
import com.gatow.salman.talashehuroof.presenter.requests.DataMuseAPI
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

/**
 * Presenter class that manipulate the board
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class BoardPresenter(private val boardEvents: BoardListener?) : Serializable {

    var board = ArrayList<ArrayList<BoardCharacter>>()
    var allWords = ArrayList<WordAvailable>()
    var selectedWord = ArrayList<BoardCharacter>()
    private var countDownTimer: CountDownTimer? = null
    private var countDownMil = 300000L

    /**
     * Add Character to update the user selection
     */
    fun addCharacter(character: BoardCharacter) {
        if (!selectedWord.contains(character)) {
            selectedWord.add(character)
            boardEvents?.updateSelectedWord(selectedWord)
        }
    }

    private fun availableWords() = ArrayList(allWords.filter { !it.strikethrough })
    private fun containsAvailable(word: String) =
        availableWords().indexOfFirst { it.word.equals(word, true) } > -1

    private fun remove(word: String) {
        val wordIndex = allWords.indexOfFirst { it.word.equals(word, true) }
        if (wordIndex >= 0)
            allWords[wordIndex].strikethrough = true
    }

    /**
     * Build an empty board
     */
    fun buildEmptyBoard(xSize: Int = 9, ySize: Int = 9): ArrayList<ArrayList<BoardCharacter>> {
        if (board.isNotEmpty()) return board
        val grid = ArrayList<ArrayList<BoardCharacter>>()
        for (y in 0..ySize) {
            val line = ArrayList<BoardCharacter>()
            for (x in 0..xSize) {
                line.add(
                    BoardCharacter(
                        "",
                        arrayOf(x, y),
                        false
                    )
                )
            }
            grid.add(line)
        }
        board = grid
        return grid
    }

    /**
     * Check selection for some AvailableWord
     */
    fun checkForWord() {
        if (selectedWord.isEmpty() || selectedWord.size == 1) {
            selectedWord.clear()
            boardEvents?.updateSelectedWord(selectedWord)
            return
        }
        var word = ""
        selectedWord.forEach { word += it.char }

        if (isALine(selectedWord) && containsAvailable(word)) {
            acceptWord(selectedWord)
            boardEvents?.updateSelectedWord(selectedWord, true)
        } else {
            selectedWord.forEach { it.isOnSelection = false }
            boardEvents?.updateSelectedWord(selectedWord)
        }
        deselectWord()
    }

    private fun deselectWord() {
        selectedWord.forEach {
            it.isOnSelection = false
        }
        selectedWord.clear()
        boardEvents?.updateSelectedWord()
    }

    private fun acceptWord(selectingWord: ArrayList<BoardCharacter>) {
        val wordSelected = getString(selectingWord)
        removeWord(wordSelected)
        boardEvents?.updateWordList(allWords)

        selectingWord.forEach {
            it.selected = true
        }

        if (availableWords().size == 0) {
            boardEvents?.onVictory()
        }
        deselectWord()
    }

    private fun setWordsCategory(category: String) {
        allWords.addAll(
            when (category) {
                //Level One Random
                "levelone" -> listOf(
                    WordAvailable("پھل"),
                    WordAvailable("کھلونا"),
                    WordAvailable("صبح"),
                    WordAvailable("لکڑی"),
                    WordAvailable("درخت"),
                    WordAvailable("عمارت")
                )

                //Level Two Fruits
                "leveltwo" -> listOf(
                    WordAvailable("آم"),
                    WordAvailable("امرود"),
                    WordAvailable("سیب"),
                    WordAvailable("آڑو"),
                    WordAvailable("انار"),
                    WordAvailable("کیلا")
                )

                //Level Three Animals
                "levelthree" -> listOf(
                    WordAvailable("بکری"),
                    WordAvailable("بھینس"),
                    WordAvailable("مارخور"),
                    WordAvailable("کینگرو"),
                    WordAvailable("ہاتھی"),
                    WordAvailable("گینڈا")
                )

                //Level Four Birds
                "levelfour" -> listOf(
                    WordAvailable("کبوتر"),
                    WordAvailable("تیتر"),
                    WordAvailable("شترمرغ"),
                    WordAvailable("شاہین"),
                    WordAvailable("چکور"),
                    WordAvailable("ابابیل")
                )

                //Level Five Cricketers
                "levelfive" -> listOf(
                    WordAvailable("بابر"),
                    WordAvailable("آفریدی"),
                    WordAvailable("فخر"),
                    WordAvailable("حسن"),
                    WordAvailable("شعیب"),
                    WordAvailable("انضمام")
                )

                //Level Six Vegetables
                "levelsix" -> listOf(
                    WordAvailable("شلجم"),
                    WordAvailable("بھنڈی"),
                    WordAvailable("بینگن"),
                    WordAvailable("گاجر"),
                    WordAvailable("کھیرہ"),
                    WordAvailable("ٹماٹر")
                )

                //Level Seven Flowers
                "levelseven" -> listOf(
                    WordAvailable("گلاب"),
                    WordAvailable("چنبیلی"),
                    WordAvailable("موتیہ"),
                    WordAvailable("گیندہ"),
                    WordAvailable("کنول"),
                    WordAvailable("سوسن")
                )

                //Level Eight Appliances
                "leveleight" -> listOf(
                    WordAvailable("استری"),
                    WordAvailable("پنکها"),
                    WordAvailable("چولھا"),
                    WordAvailable("بلب"),
                    WordAvailable("فریج"),
                    WordAvailable("ٹوکری")
                )

                //Level Nine Languages
                "levelnine" -> listOf(
                    WordAvailable("اردو"),
                    WordAvailable("پشتو"),
                    WordAvailable("سرائیکی"),
                    WordAvailable("سندھی"),
                    WordAvailable("پنجابی"),
                    WordAvailable("فارسی")
                )

                //Level Ten Days
                "levelten" -> listOf(

                    WordAvailable("سوموار"),
                    WordAvailable("منگل"),
                    WordAvailable("جمرات"),
                    WordAvailable("ہفتہ"),
                    WordAvailable("اتوار"),
                    WordAvailable("جمعہ")
                )

                //Level Eleven Countries
                "leveleleven" -> listOf(
                    WordAvailable("فلسطین"),
                    WordAvailable("پاکستان"),
                    WordAvailable("الجزائر"),
                    WordAvailable("ایران"),
                    WordAvailable("مالدیپ"),
                    WordAvailable("صومالیہ")

                )

                //Level Twelve Planets
                "leveltwelve" -> listOf(
                    WordAvailable("زھرہ"),
                    WordAvailable("زمین"),
                    WordAvailable("مریخ"),
                    WordAvailable("مشتری"),
                    WordAvailable("نیپچون"),
                    WordAvailable("یورینس")
                )
                //Don't remove this "test" it is for validation only and is used by program
                "test" -> listOf(
                    WordAvailable(
                        "Test"
                    )
                )
                /**This Else Condition will be helpful if the developer
                * Plans to add a feature of switching the game to English-Urdu
                * It will generate english words using DataMuse API and it will require
                * an active internet connection
                * the game will work normally and will not require internet as long as
                * the default levels are being played and API is not called.
                * */
                else -> DataMuseAPI()
                    .getRandomWordList(category)
            }
        )
    }

    /**
     * Reset the board with the given category
     */
    fun reset(category: String) {
        allWords.clear()
        setWordsCategory(category)
        board.forEach { line ->
            line.forEach {
                it.selected = false
                it.isOnSelection = false
            }
        }
        BoardBuilder(board).build(allWords.toTypedArray())
    }

    private fun removeWord(wordToRemove: String) {
        if (containsAvailable(wordToRemove))
            remove(wordToRemove)
        else if (containsAvailable(wordToRemove.reversed()))
            remove(wordToRemove.reversed())
    }

    private fun getString(wordSelected: ArrayList<BoardCharacter>): String {
        var word = ""
        wordSelected.forEach {
            word += it.char
        }
        return word
    }

    private fun isALine(wordSelected: ArrayList<BoardCharacter>): Boolean {

        return isHorizontalLine(wordSelected) ||
                isVerticalLine(wordSelected) ||
                isDiagonal(wordSelected)
    }

    private fun isDiagonal(wordSelected: ArrayList<BoardCharacter>): Boolean {
        val distXY = (max(wordSelected.first().position[0], wordSelected.last().position[0]) -
                min(wordSelected.first().position[0], wordSelected.last().position[0]))
        return (distXY ==
                (max(wordSelected.first().position[1], wordSelected.last().position[1]) -
                        min(wordSelected.first().position[1], wordSelected.last().position[1]))) &&
                wordSelected.size - 1 == distXY
    }

    private fun isHorizontalLine(wordSelected: ArrayList<BoardCharacter>): Boolean {
        var last = wordSelected.first()
        wordSelected.forEach {
            if (last != it) {
                if (it.position[0] != last.position[0] || kotlin.math.abs(it.position[1] - last.position[1]) != 1) return false
                last = it
            }
        }
        return true
    }

    private fun isVerticalLine(wordSelected: ArrayList<BoardCharacter>): Boolean {
        var last = wordSelected.first()
        wordSelected.forEach {
            if (last != it) {
                if (it.position[1] != last.position[1] || kotlin.math.abs(it.position[0] - last.position[0]) != 1) return false
                last = it
            }
        }
        return true
    }

    /**
     * Destroy view related component connected with this class
     */
    fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    /**
     * Start Countdown based on the difficulty of the game
     * @param difficulty defines the difficulty. For now, easy, medium and hard are available.
     * @return Return the countdown itself. Useful for testing
     */
    fun startCounter(difficulty: String?): CountDownTimer? {
        countDownMil = when (difficulty) {
            "easy" -> 300000L
            "medium" -> 150000L
            "hard" -> 120000L
            "test" -> 3000L
            else -> 300000L
        }

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(countDownMil, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countDownMil -= 1000
                boardEvents?.updateClockTime(
                    String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    )
                )
            }

            override fun onFinish() {
                if (allWords.count { !it.strikethrough } > 0)
                    boardEvents?.onLose()
            }
        }
        countDownTimer?.start()
        return countDownTimer
    }

}

/**
 * Interface of the BoardPresenter Contract
 * It's show the actions that the board does on View
 */
interface BoardListener {
    /**
     * The board presenter will trigger this method if all WordAvailable where swiped
     */
    fun onVictory()

    /**
     * The board presenter will trigger this method in a timeout
     */
    fun onLose()

    /**
     * Update word list of the UI
     */
    fun updateWordList(words: ArrayList<WordAvailable>)

    /**
     * Accept or ignore word selection
     */
    fun updateSelectedWord(
        selectingWord: ArrayList<BoardCharacter>? = null,
        acceptedWord: Boolean = false
    )

    /**
     * Update bottom clock time
     */
    fun updateClockTime(clock: String)
}