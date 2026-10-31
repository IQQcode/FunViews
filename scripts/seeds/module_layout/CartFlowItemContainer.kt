package top.iqqcode.module.layout.page

import android.content.Context
import android.widget.FrameLayout

/**
 * Story-2001 购物车条目容器：为滑动删除预留右侧操作区。
 */
class CartFlowItemContainer(context: Context) : FrameLayout(context) {

    private val actionWidth = CartFlowHelper.actionWidth(resources.displayMetrics.density)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        minimumWidth = measuredWidth + actionWidth
    }
}
