package top.iqqcode.module.animations.values

import android.animation.TypeEvaluator
import android.graphics.Point


/**
 * @Author: jiazihui
 * @Date: 2023-02-26 23:51
 * @Description:
 */
class PointEvaluator : TypeEvaluator<Any> {
    override fun evaluate(fraction: Float, startValue: Any, endValue: Any): Any {
        val startPoint: Point = startValue as Point
        val endPoint: Point = endValue as Point
        val x: Float =
            startPoint.x + fraction * (endPoint.x - startPoint.x)
        val y: Float =
            startPoint.y + fraction * (endPoint.y - startPoint.y)
        return Point(x.toInt(), y.toInt())
    }
}

