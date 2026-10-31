package top.iqqcode.module.layout.page

/**
 * Story-2001 购物车列表排版辅助：分组吸顶 + 滑动操作列宽计算。
 */
object CartFlowHelper {

    const val SWIPE_ACTION_WIDTH_DP = 72

    fun stickyOffset(groupIndex: Int, headerHeight: Int): Int = groupIndex * headerHeight

    fun actionWidth(density: Float): Int = (SWIPE_ACTION_WIDTH_DP * density).toInt()
}
