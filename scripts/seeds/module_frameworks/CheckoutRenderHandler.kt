package top.iqqcode.module.frameworks.checkout

import android.os.Handler
import android.os.Looper
import top.iqqcode.lib.common.bean.CartItem

/**
 * Story-2001 结算结果回主线程渲染的 Handler 封装（特性分支场景）。
 */
class CheckoutRenderHandler(
    private val handler: Handler = Handler(Looper.getMainLooper())
) {

    fun render(items: List<CartItem>, onSettled: (Int) -> Unit) {
        val size = items.size
        handler.post { onSettled(size) }
    }

    fun postDelayedClear(millis: Long, onCleared: () -> Unit) {
        handler.postDelayed(onCleared, millis)
    }
}
