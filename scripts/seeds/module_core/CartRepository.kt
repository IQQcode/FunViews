package top.iqqcode.module.core.cart

import top.iqqcode.lib.common.bean.CartItem

/**
 * Story-2001 购物车数据源，组件独立调试时由 debug 入口直接调用。
 */
class CartRepository {

    private val cache = linkedMapOf<String, CartItem>()

    init {
        listOf(
            CartItem("sku_1001", "联动列表 Demo", 3990L, 2),
            CartItem("sku_1002", "粒子弹射动画", 1290L, 1),
            CartItem("sku_1003", "Dock 栏组件", 5680L, 3, checked = false)
        ).forEach { cache[it.id] = it }
    }

    fun all(): List<CartItem> = cache.values.toList()

    fun updateCount(id: String, count: Int) {
        cache[id]?.let { cache[id] = it.copy(count = count.coerceAtLeast(0)) }
    }

    fun toggleChecked(id: String) {
        cache[id]?.let { cache[id] = it.copy(checked = !it.checked) }
    }
}
