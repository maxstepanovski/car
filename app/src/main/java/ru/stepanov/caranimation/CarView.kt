package ru.stepanov.caranimation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.atan2


/**
 * Лучшая вьюха из всех, что я делал XD
 *
 * @author Maksim Stepanov on 16.10.2019
 */
class CarView : RelativeLayout {
    lateinit var root: View
    lateinit var carView: ImageView
    lateinit var soundtrackEffect: MediaPlayer
    var neededAngle: Float = 0f
    var isAnimating = false


    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (context == null) {
            return
        }
        root = LayoutInflater.from(context).inflate(R.layout.car_view, this)
        carView = root.findViewById(R.id.car)

    }

    fun startParty() {
        soundtrackEffect = MediaPlayer.create(context, R.raw.soundtrack)
        soundtrackEffect.isLooping = true
        soundtrackEffect.start()
    }

    fun partyIsOver() {
        soundtrackEffect.stop()
        soundtrackEffect.release()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        if (isAnimating) {
            return false
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            neededAngle = angleBetweenPoints(
                carView.x + carView.width / 2,
                carView.y + carView.height / 2,
                event.x,
                event.y
            ).toFloat()

            val tiresSound: MediaPlayer = MediaPlayer.create(context, R.raw.tires)
            val engineSound = MediaPlayer.create(context, R.raw.engine)

            val rotationAnimator = ObjectAnimator.ofFloat(carView, "rotation", neededAngle)
            rotationAnimator.duration = 2000
            rotationAnimator.interpolator = AccelerateInterpolator()
            rotationAnimator.doOnStart {
                tiresSound.start()
                engineSound.start()
                isAnimating = true
            }
            rotationAnimator.doOnEnd {

                val animSet = AnimatorSet()
                val xTo = event.x
                val yTo = event.y
                val y = ObjectAnimator.ofFloat(carView, "translationY", yTo - height / 2 - carView.height / 2)
                val x = ObjectAnimator.ofFloat(carView, "translationX", xTo - width / 2)
                animSet.playTogether(x, y)
                animSet.interpolator = AccelerateInterpolator()
                animSet.duration = 3000
                animSet.start()
                animSet.doOnEnd {
                    isAnimating = false
                    tiresSound.reset()
                    engineSound.reset()
                }
            }
            rotationAnimator.start()
        }
        return false
    }

    private fun angleBetweenPoints(x0: Float, y0: Float, x1: Float, y1: Float): Double {
        val dY = y0 - y1
        val dX = x1 - x0
        val angle = Math.toDegrees(atan2(dX, dY).toDouble())
        if (angle >= 0) {
            return angle
        }
        return 360 + angle
    }
}