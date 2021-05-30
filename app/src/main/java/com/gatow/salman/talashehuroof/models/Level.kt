

package com.gatow.salman.talashehuroof.models

/**
 * Model class that represent a level of the board
 * @author Salman Ibrahim (salmanmaik-pk)
 * @param level level of the board (1,2,3...)
 * @param category used to generate words (like fruits, cars...)
 *
 */
data class Level(val level: Int, val category: String/*,var cleared: Boolean*/) //Boolean is to fix ++level error