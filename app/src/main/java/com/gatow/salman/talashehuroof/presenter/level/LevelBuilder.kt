

package com.gatow.salman.talashehuroof.presenter.level

import com.gatow.salman.talashehuroof.models.Level

/**
 * Presenter that generate game levels
 * @author Jordan L. Araujo Jr. (araujojordan)
 */
class LevelBuilder {

    private val levels = listOf(
        Level(1, "levelone"),
        Level(2, "leveltwo"),
        Level(3, "levelthree"),
        Level(4, "levelfour"),
        Level(5, "levelfive"),
        Level(6, "levelsix"),
        Level(7, "levelseven"),
        Level(8, "leveleight"),
        Level(9, "levelnine"),
        Level(10, "levelten"),
        Level(11, "leveleleven"),
        Level(12, "leveltwelve")
    )

    /**
     * Get levels (levels are read-only)
     */
    fun getLevels() = levels

    /**
     * Get category of a given level
     */
    fun getCategory(level: Int) = levels.firstOrNull { it.level == level }?.category ?: "levelone"

    //Level.staus is disabled
//    fun getLevelStatus(level: Int) = levels.firstOrNull { it.level == level }?.cleared ?: false

//    fun setLevelStatus(level: Int) {
//        levels[level].cleared = true
//    }
}