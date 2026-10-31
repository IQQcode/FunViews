package top.iqqcode.funviews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import top.iqqcode.funviews.R
import top.iqqcode.lib.common.bean.CartItem
import top.iqqcode.module.core.cart.CartRepository
import top.iqqcode.module.core.cart.CheckoutCalculator

/**
 * Story-2001 购物车结算汇总页：壳工程只做装配，数据与计算分别来自 module_core 与 lib_common。
 */
class CartSummaryFragment : Fragment() {

    private val repository = CartRepository()
    private val calculator = CheckoutCalculator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cart_summary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render(view, repository.all())
    }

    private fun render(view: View, items: List<CartItem>) {
        val result = calculator.compute(items, member = true)
        view.findViewById<TextView>(R.id.cart_goods_total).text = format(result.goodsInCents)
        view.findViewById<TextView>(R.id.cart_reduction).text = "-${format(result.reductionInCents)}"
        view.findViewById<TextView>(R.id.cart_payable).text = format(result.payableInCents)
    }

    private fun format(cents: Long) = "¥%.2f".format(cents / 100.0)
}
