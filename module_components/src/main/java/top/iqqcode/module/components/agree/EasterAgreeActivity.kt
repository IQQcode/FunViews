package top.iqqcode.module.components.agree

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.R
import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.content.Context

/**
 * @Author: iqqcode
 * @Date: 2025-12-07 23:25
 * @Description: 全屏氛围彩蛋点赞
 */
@Route(path = RouterAbility.COMPONENTS_EASTER_AGREE)
class EasterAgreeActivity : AppCompatActivity() {

    private lateinit var likeBtn: ImageView
    private lateinit var superLikeView: SuperLikeView

    // 逻辑控制变量
    private val handler = Handler(Looper.getMainLooper())
    private var isLongPressing = false
    private val LONG_PRESS_THRESHOLD = 500L // 500ms 触发长按
    private val EMISSION_INTERVAL = 120L // 发射间隔 120ms (类似机枪射速)

    // 震动服务
    private lateinit var vibrator: Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_easter_agree)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.easter_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        likeBtn = findViewById(R.id.like_trigger)
        superLikeView = findViewById(R.id.super_like_view)

        initVibrator()
        setupInteraction()
    }

    private fun initVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
        } else {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInteraction() {
        // 定时发射任务
        val emissionRunnable = object : Runnable {
            override fun run() {
                if (isLongPressing) {
                    fireSuperLike()
                    handler.postDelayed(this, EMISSION_INTERVAL)
                }
            }
        }

        // 长按判定任务
        val longPressCheckRunnable = Runnable {
            isLongPressing = true
            // 触发长按瞬间，先来一发
            fireSuperLike()
            // 开始循环发射
            handler.postDelayed(emissionRunnable, EMISSION_INTERVAL)
        }

        likeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下：开始计时
                    // 添加一个按下的微缩动画反馈
                    v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).start()
                    handler.postDelayed(longPressCheckRunnable, LONG_PRESS_THRESHOLD)
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开/取消
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()

                    handler.removeCallbacks(longPressCheckRunnable)
                    handler.removeCallbacks(emissionRunnable)

                    if (!isLongPressing) {
                        // 如果没达到长按阈值，视为普通点击
                        performNormalLike()
                    }

                    isLongPressing = false
                    true
                }

                else -> false
            }
        }
    }

    /**
     * 执行单次“强力点赞”动作：发射粒子 + 震动
     */
    private fun fireSuperLike() {
        // 1. 获取按钮中心位置 (作为发射点)
        val location = IntArray(2)
        likeBtn.getLocationOnScreen(location)
        val centerX = location[0] + likeBtn.width / 2f
        val centerY = location[1] + likeBtn.height / 2f // 或者 height * 0.2f 让它从上方一点出来

        // 2. 发射粒子 (一次可以发射 1 个，也可以 loop 发射 2-3 个增加密度)
        superLikeView.emit(centerX, centerY)
        // 偶尔双发
        if (Math.random() > 0.7) {
            superLikeView.emit(centerX + (Math.random() * 40 - 20).toFloat(), centerY)
        }

        // 3. 触发震动
        triggerHaptic()
    }

    /**
     * 震动反馈逻辑
     */
    private fun triggerHaptic() {
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 Predefined Effect (CLICK or HEAVY_CLICK)
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                // 老版本震动 15ms
                vibrator.vibrate(15)
            }
        }
    }

    private fun performNormalLike() {
        // 普通点赞逻辑：图标变红等
        // 这里简单演示：只震动一下，不发射粒子
        triggerHaptic()
        // 实际开发中这里切换图标状态
    }
}