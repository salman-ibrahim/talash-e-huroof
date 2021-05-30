

package com.gatow.salman.talashehuroof.models

import java.util.*

/**
 * Model class that represent a character in the board
 *
 * @author Jordan L. Araujo Jr. (araujojordan)
 * @param char The char itself in a string (so it easier to concatenate and generate string word)
 * @param position the position where index 0 is X and index 1 is Y.
 * @param isOnSelection is used to show animations on board when swiping
 * @param selected the char was already selected in a previously word
 */
data class BoardCharacter(
    var char: String,
    val position: Array<Int>,
    var isOnSelection: Boolean = false,
    var selected: Boolean = false,
    var id: Long = Date().time
) {
    /**
     * Postion in X terms
     */
    fun x() = position[1]

    /**
     * Position in Y terms
     */
    fun y() = position[0]
    override fun toString() = char
}