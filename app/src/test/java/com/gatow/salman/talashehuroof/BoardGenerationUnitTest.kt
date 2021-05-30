

package com.gatow.salman.talashehuroof

import android.os.Build
import com.gatow.salman.talashehuroof.models.WordAvailable
import com.gatow.salman.talashehuroof.presenter.board.BoardBuilder
import com.gatow.salman.talashehuroof.presenter.board.BoardPresenter
import com.gatow.salman.talashehuroof.utils.sharedExamples.BoardSharedExamples
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertTimeoutPreemptively
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.time.Duration.ofSeconds

/**
 * Unit Test for the board generation
 * @author Jordan L. Araujo Jr. (araujojordan)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class BoardGenerationUnitTest {

    @Test
    @DisplayName("Generate one board with random characters")
    fun generateOneBoard() {
        assertTimeoutPreemptively(ofSeconds(3)) {
            val boardPresenter =
                BoardBuilder(
                    BoardPresenter(
                        null
                    ).buildEmptyBoard()
                )
            boardPresenter.build(
                arrayOf(
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    ),
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    ),
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    ),
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    ),
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    ),
                    WordAvailable(
                        BoardSharedExamples.randomString()
                    )
                )
            )
//            println(boardPresenter.board)
            assertTrue(true)
        }
    }

    @Test
    @DisplayName("Generate 1000 board with random characters")
    fun generateRandomBoardsStressTest() {
        assertTimeoutPreemptively(ofSeconds(40)) {
            repeat(1000) {
                val boardPresenter =
                    BoardBuilder(
                        BoardPresenter(
                            null
                        ).buildEmptyBoard()
                    )
                boardPresenter.build(
                    arrayOf(
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        ),
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        ),
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        ),
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        ),
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        ),
                        WordAvailable(
                            BoardSharedExamples.randomString()
                        )
                    )
                )
//                println(boardPresenter.board)
            }

            assertTrue(true)
        }
    }
}
