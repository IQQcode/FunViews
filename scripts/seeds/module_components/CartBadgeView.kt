package top.iqqcode.module.components.cart

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Story-2001 购物车角标控件，本地先行提交（用于制造 diverged 场景）。
 */
class CartBadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun bind(count: Int) {
        text = if (count > 99) "99+" else count.toString()
        visibility = if (count > 0) VISIBLE else GONE
    }
}
