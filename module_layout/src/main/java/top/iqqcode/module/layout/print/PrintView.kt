package top.iqqcode.module.layout.print

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import top.iqqcode.module.layout.R
import kotlin.coroutines.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author: iqqcode
 * @Date: 2022-04-05 10:25
 * @Description:
 */
class PrintView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), LifecycleObserver {

    private lateinit var rotatingJob: Job
    private var sineWaveSamplesPath = Path()

    private var mWidth = 0f // 当前定义的View的宽
    private var mHeight = 0f // 当前定义的View的高
    private var mRadius = 0f
    private var mAngle = 10f

    // 创建实线画笔
    private val soldLinePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE // 未填充
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.colorWhite)
    }

    // 创建虚线画笔
    private val dottedLinePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE // 未填充
        strokeWidth = 8f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        color = ContextCompat.getColor(context, R.color.colorGreen)
    }

    // 创建矢量画笔
    private val vectorLinePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE // 未填充
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.colorBlue)
    }

    // 文本画笔
    private val textPaint: Paint = Paint().apply {
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        color = ContextCompat.getColor(context, R.color.colorWhite)
    }

    // 创建实线画笔
    private val fillCirclePaint: Paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE // 填充
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.colorWhite)
    }


    /**
     * View绘制的尺寸最终确定
     * @param w Int
     * @param h Int
     * @param oldw Int
     * @param oldh Int
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (w < h / 2) (w / 2).toFloat() else (h / 4).toFloat() // 半径区长宽较小值(可能是横屏)
        mRadius -= 60
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            drawAxise(this)
            drawLabel(this)
            drawDottedCircle(this)
            drawVector(this)
            drawProjections(this)
            drawSineWave(this)
        }
    }

    /**
     * 画坐标轴
     * @param canvas Canvas
     */
    private fun drawAxise(canvas: Canvas) {
        /**
         * 1. canvas.save() // 保存画布
         * 2. canvas.translate() // 移动画布到某一位置
         * 3. canvas.drawCircle() // 绘制内容
         * 4. canvas.restore() // 恢复画布
         * 以上四个方法可用系统API替代 #withTranslation()
         */
        canvas.withTranslation(mWidth / 2, mHeight / 2) { // 画布移到中心位置
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, soldLinePaint) // 绘制X轴
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, soldLinePaint) // 绘制Y轴
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) { // 画布移到中心位置
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, soldLinePaint) // 绘制X轴
        }
    }

    /**
     * 写标题
     * @param canvas Canvas
     */
    private fun drawLabel(canvas: Canvas) {
        canvas.apply {
            drawRect(100f, 100f, 600f, 250f, soldLinePaint)
            drawText("指数函数与旋转矢量", 120f, 195f, textPaint)
        }
    }

    /**
     * 画虚线
     * @param canvas Canvas
     */
    private fun drawDottedCircle(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(0f, 0f, mRadius, dottedLinePaint)
        }
    }

    /**
     * 画虚线
     * @param canvas Canvas
     */
    private fun drawVector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            withRotation(-mAngle) {
                drawLine(0f, 0f, mRadius, 0f, vectorLinePaint)
            }
        }
    }

    /**
     * 坐标点的剪切振动
     * @param canvas Canvas
     */
    private fun drawProjections(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, fillCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, fillCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            val newX = mRadius * cos(mAngle.toRadians())
            val newY = mRadius * sin(mAngle.toRadians())
            withTranslation(newX, -newY) {
                drawLine(0f, 0f, 0f, newY, soldLinePaint)
                drawLine(0f, 0f, 0f, -mHeight / 4 + newY, dottedLinePaint)
            }
        }
    }

    /**
     * 函数线
     * @param canvas Canvas
     */
    private fun drawSineWave(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            val samplesCount = 100
            val dy = mHeight / 2 / samplesCount
            sineWaveSamplesPath.reset()
            sineWaveSamplesPath.moveTo(mRadius * cos(mAngle.toRadians()), 0f) // 函数路径起点
            repeat(samplesCount) { // it为循环序号
                val x = mRadius * cos(it * -0.15 + mAngle.toRadians())
                val y = -dy * it
                sineWaveSamplesPath.quadTo(x.toFloat(), y, x.toFloat(), y)
            }
            drawPath(sineWaveSamplesPath, vectorLinePaint)
            drawTextOnPath("Android", sineWaveSamplesPath, 1000f, 20f, textPaint)
        }
    }

    /**
     * 角度变化
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating() {
        rotatingJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)
                mAngle += 10f
                invalidate()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseRotating() {
        rotatingJob.cancel()
    }

    private fun Float.toRadians() = this / 180 * PI.toFloat()
}