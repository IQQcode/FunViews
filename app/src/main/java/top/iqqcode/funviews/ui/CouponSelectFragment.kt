package top.iqqcode.funviews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import top.iqqcode.funviews.R
import top.iqqcode.lib.common.bean.Coupon
import top.iqqcode.lib.common.util.MoneyFormatter
import top.iqqcode.module.animations.animator.CouponPopAnimator
import top.iqqcode.module.components.cart.CartStepperView
import top.iqqcode.module.core.cart.CartRepository
import top.iqqcode.module.core.cart.CouponRepository
import top.iqqcode.module.frameworks.checkout.CheckoutRenderContext
import top.iqqcode.module.frameworks.checkout.CheckoutRenderPipeline
import top.iqqcode.module.frameworks.checkout.CouponStage
import top.iqqcode.module.frameworks.checkout.GoodsAmountStage
import top.iqqcode.module.frameworks.checkout.PayableStage
import top.iqqcode.module.layout.flow.CouponFlowBinder
import top.iqqcode.module.layout.flow.CouponFlowLayout

/**
 * Story-2002 优惠券选择页：壳工程只做装配——
 * 数据取自 module_core，容器与折叠来自 module_layout，控件来自 module_components，
 * 动画来自 module_animations，渲染时序由 module_frameworks 的责任链负责。
 */
class CouponSelectFragment : Fragment() {

    private val cartRepository = CartRepository()
    private val couponRepository = CouponRepository()

    private val pipeline = CheckoutRenderPipeline(
        stages = listOf(GoodsAmountStage(), CouponStage(), PayableStage())
    )

    private var selectedCoupon: Coupon? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_coupon_select, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flow = view.findViewById<CouponFlowLayout>(R.id.coupon_flow)
        val toggle = view.findViewById<TextView>(R.id.coupon_toggle)
        val stepper = view.findViewById<CartStepperView>(R.id.coupon_stepper)
        val binder = CouponFlowBinder(flow)

        val selectCoupon: (Coupon, View) -> Unit = { coupon, chip ->
            selectedCoupon = coupon
            CouponPopAnimator(chip).pop()
            render(view)
        }
        val bindCoupons: () -> Unit = {
            binder.bind(availableCoupons(), onCouponClick = selectCoupon)
            // 折叠结果要等一次 measure/layout 才落到 hasOverflow 上，这里延后同步入口状态
            view.post { binder.applyToggle(toggle) }
        }

        stepper.count = cartRepository.all().firstOrNull { it.id == DEMO_SKU }?.count ?: 1
        stepper.onCountChanged = { count ->
            cartRepository.updateCount(DEMO_SKU, count)
            bindCoupons()
            render(view)
        }

        bindCoupons()
        render(view)
    }

    private fun availableCoupons(): List<Coupon> =
        couponRepository.available(goodsInCents(), member = true)

    private fun goodsInCents(): Long = cartRepository.all().sumOf { it.subtotalInCents }

    private fun render(view: View) {
        val context = CheckoutRenderContext(
            items = cartRepository.all(),
            coupons = availableCoupons(),
            coupon = selectedCoupon,
            member = true
        )
        pipeline.submit(context) { rendered ->
            if (!view.isAttachedToWindow) return@submit
            val summary = rendered.lines.joinToString("\n") { "${it.label}　${it.value}" }
            val payable = MoneyFormatter.format(rendered.payableInCents)
            view.findViewById<TextView>(R.id.coupon_summary).text = "$summary\n应付　$payable"
        }
    }

    companion object {
        private const val DEMO_SKU = "sku_1001"
    }
}
