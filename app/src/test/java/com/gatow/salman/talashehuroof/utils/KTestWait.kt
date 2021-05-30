

package com.gatow.salman.talashehuroof.utils

import android.os.Looper
import org.robolectric.Shadows
import java.util.*

/**
 * Unit Test helper for async parts of code on Roboeletric
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class KTestWait<T>(private var millisseconds: Long = 3000) {

    private var timeOfStarted: Long? = null
    private var waitObject: T? = null

    /**
     * This method will idle the the app that are using RoboEletric until the send method send
     * the required object or it reach the timeout defined in the millisseconds variable
     *
     * It's looks slow as it is a busy wait. But this code release all the background threads that
     * are pending, so the code will run it on background will rapidly be executed, even if have multiple
     * threads OR Coroutines to run.
     */
    fun receive(): T? {
        timeOfStarted = Date().time
        while (waitObject == null && !overtime()) Shadows.shadowOf(Looper.getMainLooper()).idle()
        return waitObject
    }

    /**
     * Interrupt the receive() loop
     */
    fun send(waitObject: T) {
        this.waitObject = waitObject
    }

    /**
     * Force stop the loop (it will be as timetout)
     */
    fun stop() {
        timeOfStarted = timeOfStarted?.plus(millisseconds)
    }

    /**
     * Check for overtime for reach the timeout
     */
    private fun overtime(): Boolean {
        return Date().time >= (timeOfStarted ?: Date().time) + millisseconds
    }
}