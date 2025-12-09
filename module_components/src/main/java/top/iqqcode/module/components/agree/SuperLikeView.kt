package top.iqqcode.module.components.agree

/**
 * @Author: iqqcode
 * @Date: 2025-12-08 00:34
 * @Description:
 */


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import top.iqqcode.module.components.R
import java.util.LinkedList
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin

/**
 * 高性能全屏点赞粒子视图
 * 使用 Canvas 直接绘制 + 对象池复用
 */
class SuperLikeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // ==========================================
    // 1. 数据结构定义
    // ==========================================

    /**
     * 粒子对象
     * @param x 当前X坐标
     * @param y 当前Y坐标
     * @param vX X轴速度 (px/ms)
     * @param vY Y轴速度 (px/ms) - 负值向上
     * @param alpha 透明度 0-255
     * @param scale 缩放比例
     * @param bitmap 对应的图片资源
     * @param lifeTime 已存活时间 (ms)
     * @param maxLifeTime 最大生命周期 (ms)
     */
    private class Particle {
        var x: Float = 0f
        var y: Float = 0f
        var vX: Float = 0f
        var vY: Float = 0f
        var alpha: Int = 255
        var scale: Float = 0f
        var bitmap: Bitmap? = null
        var lifeTime: Long = 0
        var maxLifeTime: Long = 0

        // 重置方法，用于对象池复用
        fun reset() {
            x = 0f
            y = 0f
            vX = 0f
            vY = 0f
            alpha = 255
            scale = 0f
            bitmap = null
            lifeTime = 0
            maxLifeTime = 0
        }
    }

    // ==========================================
    // 2. 成员变量
    // ==========================================

    private val particles = LinkedList<Particle>() // 当前活跃的粒子
    private val particlePool = LinkedList<Particle>() // 粒子对象池 (回收站)
    private val paint = Paint().apply { isAntiAlias = true }
    private val random = Random()
    private val bitmaps = mutableListOf<Bitmap>() // 缓存 Bitmap 资源

    private var lastFrameTime: Long = 0

    // 配置参数
    private val gravity = 0.0008f // 模拟重力/阻力 (px/ms^2)
    private val targetScale = 1.0f // 最终缩放大小
    private val popScale = 1.2f // 弹出时的最大缩放

    init {
        // 预加载资源为 Bitmap，避免绘制时解码
        val drawableIds = listOf(
            R.drawable.ic_pop_heart,
            R.drawable.ic_pop_star,
            R.drawable.ic_pop_text_strong,
            R.drawable.ic_pop_text_wow
        )

        drawableIds.forEach { id ->
            val drawable = ContextCompat.getDrawable(context, id)
            if (drawable is VectorDrawable || drawable != null) {
                // 转为 Bitmap，大小设为固定值，例如 150x150
                bitmaps.add(drawable.toBitmap(150, 150))
            }
        }
    }

    // ==========================================
    // 3. 核心逻辑：发射与更新
    // ==========================================

    /**
     * 发射一个粒子
     * @param startX 发射源 X
     * @param startY 发射源 Y
     */
    fun emit(startX: Float, startY: Float) {
        // 1. 从池中取，或者 new 一个
        val p = if (particlePool.isNotEmpty()) particlePool.removeFirst() else Particle()
        p.reset()

        // 2. 初始化参数
        p.x = startX
        p.y = startY

        // 随机素材
        if (bitmaps.isNotEmpty()) {
            p.bitmap = bitmaps[random.nextInt(bitmaps.size)]
        }

        // 角度：向上扇形 (-120度 到 -60度，因为Android Y轴向下，所以向上是负角度)
        // 视频中大约是左右各30度，即 -90 +/- 30 -> -120 到 -60
        // 或者更简单的 math: random.nextGaussian

        // 设定速度向量
        val angle = Math.toRadians(-90.0 + (random.nextDouble() * 60.0 - 30.0)) // -90 ± 30度
        val speed = 1.5f + random.nextFloat() * 1.5f // 速度 1.5 ~ 3.0 px/ms

        p.vX = (speed * cos(angle)).toFloat()
        p.vY = (speed * sin(angle)).toFloat()

        p.maxLifeTime = 1000L + random.nextInt(500) // 存活 1000~1500ms

        particles.add(p)

        // 触发重绘
        if (particles.size == 1) {
            lastFrameTime = System.currentTimeMillis()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (particles.isEmpty()) return

        val currentTime = System.currentTimeMillis()
        // 计算两帧间隔，用于平滑动画 (Delta Time)
        var dt = (currentTime - lastFrameTime)
        if (dt > 50) dt = 16 // 防止切后台后回来第一帧跳跃过大
        lastFrameTime = currentTime

        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()

            // --- 更新物理状态 ---
            p.lifeTime += dt

            // 移动
            p.x += p.vX * dt
            p.y += p.vY * dt

            // 简单阻力/重力模拟：Y轴速度逐渐变慢(甚至下落)
            p.vY += gravity * dt

            // --- 更新视觉状态 ---
            // 1. 缩放逻辑 (Elastic Pop): 0 -> 1.2 -> 1.0
            val progress = p.lifeTime.toFloat() / p.maxLifeTime

            if (p.lifeTime < 200) {
                // 前200ms：放大到 1.2
                val t = p.lifeTime / 200f
                p.scale = t * popScale
            } else if (p.lifeTime < 400) {
                // 200-400ms：回弹到 1.0
                val t = (p.lifeTime - 200) / 200f
                p.scale = popScale - t * (popScale - targetScale)
            } else {
                p.scale = targetScale
            }

            // 2. 透明度逻辑 (Fade Out): 最后 300ms 渐隐
            if (p.lifeTime > p.maxLifeTime - 300) {
                val fadeProgress = (p.maxLifeTime - p.lifeTime) / 300f // 1 -> 0
                p.alpha = (fadeProgress * 255).toInt().coerceIn(0, 255)
            }

            // --- 绘制 ---
            if (p.bitmap != null) {
                paint.alpha = p.alpha

                // 矩阵变换绘制 (为了缩放中心在图片中间)
                val halfW = p.bitmap!!.width / 2f * p.scale
                val halfH = p.bitmap!!.height / 2f * p.scale

                // 定义绘制区域
                val left = p.x - halfW
                val top = p.y - halfH
                val right = p.x + halfW
                val bottom = p.y + halfH

                canvas.save()
                // 也可以在这里加旋转 canvas.rotate(...)
                canvas.drawBitmap(
                    p.bitmap!!,
                    null,
                    android.graphics.RectF(left, top, right, bottom),
                    paint
                )
                canvas.restore()
            }

            // --- 死亡判定 ---
            if (p.lifeTime >= p.maxLifeTime) {
                iterator.remove() // 从活跃列表移除
                particlePool.add(p) // 加入对象池
            }
        }

        // 如果还有粒子，请求下一帧
        if (particles.isNotEmpty()) {
            postInvalidateOnAnimation()
        }
    }
}