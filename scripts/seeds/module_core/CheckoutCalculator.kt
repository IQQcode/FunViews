package top.iqqcode.module.core.cart

import top.iqqcode.lib.common.bean.CartItem
import kotlin.math.roundToLong

/**
 * Story-2001 结算价计算：满减 + 会员折扣，结果以「分」为单位回传。
 */
class CheckoutCalculator(
    private val fullReductionThreshold: Long = 100_00L,
    private val fullReductionAmount: Long = 10_00L,
    private val memberRate: Double = 0.95
) {

    fun compute(items: List<CartItem>, member: Boolean): CheckoutResult {
        val goods = items.sumOf { it.subtotalInCents }
        val reductions = (goods / fullReductionThreshold).toInt() * fullReductionAmount
        val discounted = if (member) ((goods - reductions) * memberRate).roundToLong() else goods - reductions
        return CheckoutResult(
            goodsInCents = goods,
            reductionInCents = reductions.coerceAtMost(goods),
            payableInCents = (discounted - reductions).coerceAtLeast(0L)
        )
    }

    data class CheckoutResult(
        val goodsInCents: Long,
        val reductionInCents: Long,
        val payableInCents: Long
    )
}
