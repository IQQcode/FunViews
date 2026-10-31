package top.iqqcode.module.animations.animator

import android.widget.TextView

/**
 * Story-2001 加购弹跳动画（工作区脏状态场景：已暂存后又有未暂存改动）。
 */
class CartBounceAnimator(private val target: TextView) {

    fun bounce() {
        target.animate()
            .scaleX(1.25f)
            .scaleY(1.25f)
            .setDuration(120L)
            .withEndAction {
                target.animate().scaleX(1f).scaleY(1f).setDuration(90L).start()
            }
            .start()
    }

    fun shake() {
        target.animate()
            .translationXBy(8f)
            .setDuration(60L)
            .withEndAction { target.animate().translationXBy(-8f).setDuration(60L).start() }
            .start()
    }
}
