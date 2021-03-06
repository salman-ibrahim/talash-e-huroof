

package com.gatow.salman.talashehuroof.utils

import android.util.Log
import com.gatow.salman.talashehuroof.models.BoardCharacter
import kotlin.math.absoluteValue
import kotlin.math.atan2

/**
 * Make a straight line from first to last letter selected, restricting to the 8 directions
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class LineSelectionHelper {
    private var firstChar: BoardCharacter? = null
    private var lastChar: BoardCharacter? = null
    private var grid: ArrayList<ArrayList<BoardCharacter>>? = null
    private var originPoint = arrayOf<Int>()

    /**
     * Add the first letter and the grid
     * @param boardChar first letter of the selection
     * @param grid that will perform the selection
     */
    fun addFirst(boardChar: BoardCharacter, grid: ArrayList<ArrayList<BoardCharacter>>) {
        require(firstChar == null)
        this.firstChar = boardChar
        this.grid = grid
        originPoint = firstChar?.position ?: arrayOf(0, 0)
        Log.d("LineSelectionHelper", "ORIGINAL: x:${originPoint[1]} y:${originPoint[0]}")
    }

    /**
     * Draw a line from first letter until the boardChar letter
     * @param boardChar last letter of the selection
     * @return straight line from first to boardChar letter
     */
    fun showLine(boardChar: BoardCharacter): List<BoardCharacter> {
        require(firstChar != null)

        lastChar = boardChar

        //Get point
        val endPoint = boardChar.position

        //Fix origin point as the first point and fix phone orientation
        val x = endPoint[1] - originPoint[1]
        val y = -(endPoint[0] - originPoint[0])

        //Get Angle
        val angle = (atan2(x.toDouble(), y.toDouble()) * (180 / Math.PI) + 360) % 360
        val anglePart = 360.0 / 16.0

        val distXY = (if (x.absoluteValue > y.absoluteValue) x else y).absoluteValue
        Log.d("LineSelectionHelper", "dist:$distXY")
        val list = mutableListOf<BoardCharacter>()
        list.add(firstChar!!)
        return when {
            //NORTHEAST
            checkRange(angle, anglePart, anglePart * 3) -> {
                Log.d("LineSelectionHelper", "NORTHEAST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() + 1, list.last().y() - 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //EAST
            checkRange(angle, anglePart * 3, anglePart * 5) -> {
                Log.d("LineSelectionHelper", "EAST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() + 1, list.last().y()))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //SOUTHEAST
            checkRange(angle, anglePart * 5, anglePart * 7) -> {
                Log.d("LineSelectionHelper", "SOUTHEAST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() + 1, list.last().y() + 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //SOUTH
            checkRange(angle, anglePart * 7, anglePart * 9) -> {
                Log.d("LineSelectionHelper", "SOUTH")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x(), list.last().y() + 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //SOUTHWEST
            checkRange(angle, anglePart * 9, anglePart * 11) -> {
                Log.d("LineSelectionHelper", "SOUTHWEST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() - 1, list.last().y() + 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //WEST
            checkRange(angle, anglePart * 11, anglePart * 13) -> {
                Log.d("LineSelectionHelper", "WEST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() - 1, list.last().y()))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //NORTHWEST
            checkRange(angle, anglePart * 13, anglePart * 15) -> {
                Log.d("LineSelectionHelper", "NORTHWEST")
                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x() - 1, list.last().y() - 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
            //NORTH
            else -> {
                Log.d("LineSelectionHelper", "NORTH")

                repeat(distXY) {
                    try {
                        list.add(getChar(list.last().x(), list.last().y() - 1))
                    } catch (err: Exception) {
                        /** Ignore if selects to a outside letter **/
                    }
                }
                list
            }
        }
    }

    private fun getChar(x: Int, y: Int) = grid!![x][y]

    private fun checkRange(angle: Double, from: Double, to: Double): Boolean {
        if (from < 0.0 && to >= 0.0)
            return checkRange(angle, from, 0.0) &&
                    checkRange(angle, 0.0, to)
        return (angle in from..to)
    }

}