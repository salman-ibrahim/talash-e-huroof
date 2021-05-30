

package com.gatow.salman.talashehuroof.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation


/**
 * Do a bouncing animation one time only in a view
 */
fun bounceAnimation(view: View) {
    view.startAnimation(
        ScaleAnimation(
            1f, 1.4f,
            1f, 1.4f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 300
            fillAfter = false
            interpolator = BounceInterpolator()
        })
}