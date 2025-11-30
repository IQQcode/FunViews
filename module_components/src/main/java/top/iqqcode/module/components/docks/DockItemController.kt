package top.iqqcode.module.components.docks

import android.view.View
import android.widget.TextView

/**
 * @Author: iqqcode
 * @Date: 2025-11-30 12:59
 * @Description:
 */
class DockItemController(
    val container: View,
    val content: TextView,
    val baseSize: Float
) {
    var currentSize: Float = baseSize

    fun applyChanges() {
        val newWidth = currentSize.toInt()
        val params = container.layoutParams

        // 1. 只修改宽度！绝对不修改高度！
        // 解决了“高度抖动”问题
        if (params.width != newWidth) {
            params.width = newWidth
            // params.height 保持原样，不动！
            container.layoutParams = params
        }

        // 2. 视觉缩放
        // scaleFactor = 现在的宽度 / 原始宽度
        // 这样内容会视觉变大，超出容器高度(向上生长)，但不影响物理布局高度
        val scaleFactor = currentSize / baseSize

        if (content.scaleX != scaleFactor) {
            content.scaleX = scaleFactor
            content.scaleY = scaleFactor
        }
    }
}