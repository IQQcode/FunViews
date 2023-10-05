package top.iqqcode.module.animations.values

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View

/**
 * @Author: jiazihui
 * @Date: 2023-02-26 23:58
 * @Description:
 */
class PathAnimView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var currentPoint: Point? = null
    private val mPaint: Paint = Paint()

    override fun onDraw(canvas: Canvas) {
        if (currentPoint == null) {
            currentPoint = Point(RADIUS, RADIUS)
            drawCircle(canvas)
            startAnimation()
        } else {
            drawCircle(canvas)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        val x = currentPoint!!.x
        val y = currentPoint!!.y
        canvas.drawCircle(x.toFloat(), y.toFloat(), RADIUS.toFloat(), mPaint)
    }

    private fun startAnimation() {
        val startPoint = Point(RADIUS, RADIUS)
        val endPoint = Point(width - RADIUS, height - RADIUS)
        val anim = ValueAnimator.ofObject(PointEvaluator(), startPoint, endPoint)
        anim.addUpdateListener { animation ->
            currentPoint = animation.animatedValue as Point
            invalidate()
        }
        anim.duration = 5000
        anim.start()
    }

    companion object {
        const val RADIUS = 50
    }
}