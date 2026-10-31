package top.iqqcode.lib.common.router

import androidx.annotation.StringDef
import top.iqqcode.lib.common.router.CartRouter.Companion.CART_SUMMARY_FRAGMENT

/**
 * Story-2001 购物车结算链路的路由契约，由壳工程与各组件仓共享。
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(CART_SUMMARY_FRAGMENT)
annotation class CartRouter {
    companion object {

        const val CART_SUMMARY_FRAGMENT = "/cart/CartSummaryFragment"

        const val ARG_CART_ID = "arg_cart_id"
    }
}
