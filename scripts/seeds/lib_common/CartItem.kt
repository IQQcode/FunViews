package top.iqqcode.lib.common.bean

/**
 * Story-2001 购物车条目的跨仓数据契约，字段变更需要组件仓同步适配。
 */
data class CartItem(
    val id: String,
    val title: String,
    val priceInCents: Long,
    val count: Int,
    val checked: Boolean = true
) {
    val subtotalInCents: Long
        get() = if (checked) priceInCents * count else 0L
}
