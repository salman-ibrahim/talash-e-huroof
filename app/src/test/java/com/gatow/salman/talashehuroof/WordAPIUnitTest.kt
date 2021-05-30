

package com.gatow.salman.talashehuroof

import android.os.Build
import com.gatow.salman.talashehuroof.models.WordAvailable
import com.gatow.salman.talashehuroof.presenter.requests.DataMuseAPI
import com.gatow.salman.talashehuroof.utils.KTestWait
import io.sniffy.socket.DisableSockets
import io.sniffy.test.junit.SniffyRule
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

/**
 * Unit tests for the word API
 * @author Jordan L. Araujo Jr. (araujojordan)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class WordAPIUnitTest {

    @get:Rule
    var sniffyRule = SniffyRule()

    @Test
    @DisplayName("Test to get 10 words from the API")
    fun getWords() {
        val waitTest = KTestWait<List<WordAvailable>?>(3000)
        waitTest.send(
            DataMuseAPI()
                .getRandomWordList("fruit", 10))
        val list = waitTest.receive()
        println(list)
        assertTrue(list?.size == 10)
    }

    @Test
    @DisplayName("Test to check if word are random for a same category")
    fun wordsAreRandomFromSameCategory() {
        val waitTest = KTestWait<List<WordAvailable>?>(3000)
        waitTest.send(
            DataMuseAPI()
                .getRandomWordList("fruit", 10))
        val list1 = waitTest.receive()

        val waitTest2 = KTestWait<List<WordAvailable>?>(3000)
        waitTest2.send(
            DataMuseAPI()
                .getRandomWordList("fruit", 10))
        val list2 = waitTest2.receive()

        println(list1)
        println(list2)


        var repeatedWords = 0
        list1?.forEachIndexed { index, word ->
            if (word.word == list2?.get(index)?.word) repeatedWords++
        }

        // It will be statistically hard to get 3 words at the same position of 2 random lists
        assertTrue(repeatedWords < 3)
    }

    @Test
    @DisableSockets
    @DisplayName("Test to check if the API throws an exception on no internet access")
    fun noInternetConnection() {
        Assertions.assertThrows(Exception::class.java) {
            val waitTest = KTestWait<List<WordAvailable>?>(3000)
            waitTest.send(
                DataMuseAPI()
                    .getRandomWordList("پھل", 10))
            val list = waitTest.receive()
            println(list)
            assertTrue(false)
        }
    }

    @Test
    @DisplayName("Test DatamuseAPI with a strange category that doesn't give enough words")
    fun notEnoughWords() {
        Assertions.assertThrows(Exception::class.java) {
            val waitTest = KTestWait<List<WordAvailable>?>(3000)
            waitTest.send(
                DataMuseAPI()
                    .getRandomWordList("nowords", 10))
            val list = waitTest.receive()
            println(list)
            assertTrue(list?.size == 10)
        }
    }
}
