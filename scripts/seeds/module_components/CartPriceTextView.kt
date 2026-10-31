package top.iqqcode.module.components.cart

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Story-2001 价格文案控件，由组件仓远端先行提交（用于制造 diverged 场景）。
 */
class CartPriceTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun bind(priceInCents: Long) {
        text = "¥%.2f".format(priceInCents / 100.0)
    }
}
