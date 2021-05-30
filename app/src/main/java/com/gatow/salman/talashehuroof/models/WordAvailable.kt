

package com.gatow.salman.talashehuroof.models

/**
 * Model class that represent a WordAvailable
 * @author Jordan L. Araujo Jr. (araujojordan)
 * @param word that will be displayed in the board
 * @param strikethrough if the word was already found and swiped
 * @param didAnimation if the found animation was already be triggered
 *
 */
data class WordAvailable(
    var word: String,
    var strikethrough: Boolean = false,
    var didAnimation: Boolean = false
) {
    override fun toString() = word
}