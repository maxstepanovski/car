package ru.stepanov.caranimation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 *
 * @author Maksim Stepanov on 16.10.2019
 */
class CarView : View {
    lateinit var rectPaint: Paint
    lateinit var pathPaint: Paint

    var carWidth: Int = 50
    var carLength: Int = 100
    var turningRadius: Int = 0
    var speed: Int = 0
    var color: Int = 0
    var path: Path = Path()

    var isAnimating = false
    var justStarted = true
    var touchPoint = Point()
    var currentPoint = Point()

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
        if (attrs == null) {
            turningRadius = carWidth * 3
            speed = 1
            color = resources.getColor(android.R.color.black, null)
        } else {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarView, 0, 0)
            try {
                carWidth = typedArray.getInt(R.styleable.CarView_carWidth, 50)
                carLength = typedArray.getInt(R.styleable.CarView_carLength, 100)
                turningRadius = typedArray.getInt(R.styleable.CarView_carTurningRadius, carWidth * 3)
                speed = typedArray.getInt(R.styleable.CarView_carSpeed, 1)
                color =
                    typedArray.getColor(R.styleable.CarView_carColor, resources.getColor(android.R.color.black, null))
            } finally {
                typedArray.recycle()
            }
        }
        rectPaint = Paint()
        rectPaint.color = color
        rectPaint.isAntiAlias = true


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        if (isAnimating) {
            if (calculateDistance(currentPoint, touchPoint) <= carWidth / 2) {
                isAnimating = false
            } else {


            }
        } else {
            if (justStarted) {
                currentPoint.x = width / 2
                currentPoint.y = height / 2
            }
        }
        drawRect(currentPoint.x, currentPoint.y, canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        if (isAnimating) {
            return false
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchPoint = Point(event.x.toInt(), event.y.toInt())
            isAnimating = true
            justStarted = false

            path.reset()
            path.moveTo(currentPoint.x.toFloat(), currentPoint.y.toFloat())
            path.lineTo(touchPoint.x.toFloat(), touchPoint.y.toFloat())
            val measure = PathMeasure(path, false)

            rectPaint.pathEffect = PathDashPathEffect(path, measure.length, 0.toFloat(), PathDashPathEffect.Style.TRANSLATE)

            invalidate()
        }
        return false
    }

    private fun calculateDistance(p1: Point, p2: Point): Int {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x).toDouble(), 2.0) + Math.pow(Math.abs(p1.y - p2.y).toDouble(), 2.0)).toInt()
    }

    private fun drawRect(x: Int, y: Int, canvas: Canvas) {
        val halfWidth = carWidth / 2
        val halfLength = carLength / 2

        canvas.drawRect(
            (x - halfWidth).toFloat(),
            (y + halfLength).toFloat(),
            (x + halfWidth).toFloat(),
            (y - halfLength).toFloat(),
            rectPaint
        )
    }
}