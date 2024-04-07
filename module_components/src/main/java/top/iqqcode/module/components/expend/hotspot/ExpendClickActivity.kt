package top.iqqcode.module.components.expend.hotspot

import android.graphics.Rect
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.ComponentsRouter
import top.iqqcode.lib.common.util.UtilHelper
import top.iqqcode.module.components.databinding.ActivityExpendClickBinding


/**
 * 扩大点击热区
 *
 * @constructor Create empty Expend click activity
 */
@Route(path = ComponentsRouter.EXPEND_CLICK_ACTIVITY)
class ExpendClickActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpendClickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpendClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mExpendButton.setOnClickListener {
            expendTouchArea(binding.clickTargetView,
                0,
                0,
                UtilHelper.dip2px(this, 100F),
                UtilHelper.dip2px(this, 100F))
        }

        binding.mParentRootView.setOnClickListener {
            expendTouchArea(binding.clickTargetView,
                0, 0, 0,
                UtilHelper.dip2px(this, 200F))
        }

        binding.clickTargetView.setOnClickListener {
            Toast.makeText(this, "粉色被点击", Toast.LENGTH_SHORT).show()
        }

        binding.mParentRootView.setOnClickListener {
            Toast.makeText(this, "灰色Root点击", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 扩展点击区域的范围
     *
     * @param view       需要扩展的元素，此元素必需要有父级元素
     * @param expendSize 需要扩展的尺寸
     */
    private fun expendTouchArea(view: View?, expendSize: Int) {
        expendTouchArea(view, expendSize, expendSize, expendSize, expendSize)
    }

    private fun expendTouchArea(view: View?, left: Int, top: Int, right: Int, bottom: Int) {
        if (view == null) {
            return
        }

        val parentView = view.parent as View
        parentView.post {
            val rect = Rect()
            view.getHitRect(rect) // 如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
            rect.left -= left
            rect.top -= top
            rect.right += right
            rect.bottom += bottom
            parentView.touchDelegate = TouchDelegate(rect, view)
        }
    }
}