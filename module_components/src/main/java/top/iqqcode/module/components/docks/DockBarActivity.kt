package top.iqqcode.module.components.docks

import android.graphics.Rect
import android.os.Bundle
import android.view.Choreographer
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.FrameworksRouter
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.ActivityComponentsBinding
import top.iqqcode.module.components.databinding.ActivityDockBarBinding
import kotlin.math.abs

/**
 * @Author: iqqcode
 * @Date: 2025-11-30 09:38
 * @Description: Dock效果模仿
 * Dock栏H5效果实现：https://codepen.io/mnilzg/pen/oNBvXxB
 */
@Route(path = RouterAbility.COMPONENTS_DOCK_BAR)
class DockBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDockBarBinding

    // === 核心配置 ===
    // 初始设定一个理想值，后面会根据屏幕宽度自动缩小
    private var baseWidthDp = 50f
    private var maxScale = 1.8f
    private val influenceRadiusDp = 150f

    // 间距 (Visual Gap)
    private val itemGapDp = 5f

    // === 像素缓存 ===
    private var baseWidthPx = 0
    private var itemGapPx = 0
    private var influenceRadiusPx = 0f

    // 静态虚拟网格：用于解决偏移问题
    // 记录每个 Item 在未缩放状态下的中心点 X 坐标
    private val staticItemCenters = mutableListOf<Float>()

    // === 状态 ===
    private var touchX: Float? = null
    private val dockItems = mutableListOf<DockItemController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDockBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 1. 屏幕适配 (防止溢出屏幕的关键)
        adjustParamsToFitScreen()

        // 2. 初始化图标
        setupDockItems()

        // 3. 启动动画帧循环
        startAnimationLoop()
    }


    /**
     * 核心修复：根据屏幕宽度，自动计算合适的图标大小。
     * 确保：(图标总宽 * 膨胀系数) < 屏幕宽度
     */
    private fun adjustParamsToFitScreen() {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val density = displayMetrics.density

        val emojiCount = 7

        // 预估最大膨胀宽度：假设所有图标都处于半放大状态的极端情况 + 两侧 Padding
        // 简单的安全算法：总数 * 基础宽 * 1.3 (平均膨胀率)
        // 如果屏幕太小，就缩小 baseWidth

        val safetyMargin = 40 * density // 两侧留白
        val availableWidth = screenWidth - safetyMargin

        // 计算当前配置需要的宽度
        val currentNeededWidth = emojiCount * (baseWidthDp * density * 1.4f) // 1.4是预估膨胀缓冲

        if (currentNeededWidth > availableWidth) {
            // 如果超宽，反向计算合适的 baseWidth
            val safeBaseWidthPx = availableWidth / (emojiCount * 1.4f)
            baseWidthPx = safeBaseWidthPx.toInt()
            // 同时也稍微减小放大倍率，防止挤压太严重
            maxScale = 1.6f
        } else {
            baseWidthPx = (baseWidthDp * density).toInt()
        }

        itemGapPx = (itemGapDp * density).toInt()
        influenceRadiusPx = influenceRadiusDp * density
    }

    private fun setupDockItems() {
        // val emojis = listOf("😜", "😍", "🤩", "🥳", "🥶")
        val emojis = listOf("😃", "😊", "😜", "😍", "🤩", "🥳", "🥶")

        binding.dockBar.removeAllViews()
        dockItems.clear()
        staticItemCenters.clear()

        emojis.forEach { emoji ->
            // 1. 外层容器：负责物理占位 (只变宽，不变高)
            val container = FrameLayout(this).apply {
                // 高度固定为 baseWidthPx，绝对不修改它，解决高度抖动！
                layoutParams = LinearLayout.LayoutParams(baseWidthPx, baseWidthPx).apply {
                    gravity = Gravity.BOTTOM
                    // 设置间距
                    setMargins(itemGapPx, 0, itemGapPx, 0)
                }
                clipChildren = false
                clipToPadding = false
            }

            // 2. 内容视图：负责视觉放大
            val textView = TextView(this).apply {
                text = emoji
                // 根据 baseWidthPx 动态计算字号，防止圆圈小字大
                textSize = (baseWidthPx / resources.displayMetrics.density) * 0.45f
                gravity = Gravity.CENTER
                includeFontPadding = false

                // 大小固定填满容器
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER
                }

                // 关键：锚点设为底部中心
                pivotX = baseWidthPx / 2f
                pivotY = baseWidthPx.toFloat()
            }

            container.addView(textView)
            binding.dockBar.addView(container)

            dockItems.add(DockItemController(container, textView, baseWidthPx.toFloat()))
        }

        // 3. 计算静态网格 (Static Grid)
        // 这一步必须在 Layout 完成后或者手动预计算
        // 这里我们采用简单的预计算逻辑，假设 Dock 居中
        binding.dockBar.post {
            calculateStaticGrid()
        }

        // 触摸监听
        binding.dockBar.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    touchX = event.rawX
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    touchX = null
                }
            }
            true
        }
    }

    /**
     * 计算每个 Item 在屏幕上的“理想归位坐标”
     * 这解决了偏移问题：手指永远和“它应该在的位置”做比较，而不是“它现在的位置”
     */
    private fun calculateStaticGrid() {
        val dockRect = Rect()
        binding.dockBar.getGlobalVisibleRect(dockRect)

        // Dock 栏的中心
        val dockCenterX = dockRect.centerX().toFloat()

        // 总静态宽度 (包含 margin)
        val totalWidth = dockItems.size * baseWidthPx + (dockItems.size * 2 * itemGapPx)
        val startX = dockCenterX - (totalWidth / 2f)

        staticItemCenters.clear()
        for (i in dockItems.indices) {
            // 计算第 i 个 item 的中心 X
            val itemWidthWithGap = baseWidthPx + (2 * itemGapPx)
            val centerX = startX + (i * itemWidthWithGap) + (itemWidthWithGap / 2f)
            staticItemCenters.add(centerX)
        }
    }

    private fun startAnimationLoop() {
        val choreographer = Choreographer.getInstance()
        val frameCallback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                updateDockLayout()
                choreographer.postFrameCallback(this)
            }
        }
        choreographer.postFrameCallback(frameCallback)
    }

    private fun updateDockLayout() {
        val currentTouchX = touchX

        // 如果 Dock 栏位置发生变化(比如旋转屏幕)，需要重新计算网格
        // 这里为了性能省略，实际项目可以在 onConfigurationChanged 触发 calculateStaticGrid

        dockItems.forEachIndexed { index, item ->
            var targetSize = baseWidthPx.toFloat()

            if (currentTouchX != null && index < staticItemCenters.size) {
                // 核心：使用静态中心点计算距离
                val staticCenterX = staticItemCenters[index]
                val distance = abs(currentTouchX - staticCenterX)

                if (distance < influenceRadiusPx) {
                    val progress = (1f - (distance / influenceRadiusPx)).coerceIn(0f, 1f)
                    // 线性插值
                    val scale = 1f + (maxScale - 1f) * progress
                    targetSize = baseWidthPx * scale
                }
            }

            // 动画平滑处理
            val smoothFactor = 0.2f
            if (abs(item.currentSize - targetSize) < 0.5f) {
                item.currentSize = targetSize
            } else {
                item.currentSize += (targetSize - item.currentSize) * smoothFactor
            }

            item.applyChanges()
        }
    }
}
