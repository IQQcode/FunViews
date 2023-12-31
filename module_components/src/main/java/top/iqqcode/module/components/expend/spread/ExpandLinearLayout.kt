package top.iqqcode.module.components.expend.spread

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginTop

/**
 * @Author: jiazihui
 * @Date: 2023-04-09 20:13
 * @Description:
 */
class ExpandLinearLayout : LinearLayout {

    // 是否展开，默认展开
    private var isOpen = true

    // 第一个子view的高度，即收起保留高度
    private var firstChildHeight = 0

    // 所有子view高度，即总高度
    private var allChildHeight = 0

    /**
     * 动画值改变的时候 请求重新布局
     */
    private var animPercent: Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        // 横向的话 稍加修改计算宽度即可
        orientation = VERTICAL
        animPercent = 1f
        isOpen = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 重置高度
        allChildHeight = 0
        firstChildHeight = 0

        if (childCount > 0) {
            // 遍历计算高度
            for (index in 0 until childCount) {
                // 实际使用中除了measuredHeight，以及margin等，也要计算在内
                if (index == 0) {
                    firstChildHeight = getChildAt(index).measuredHeight
                    +getChildAt(index).marginTop + getChildAt(index).marginBottom
                    +this.paddingTop + this.paddingBottom
                }
                // 实际使用时或包括padding等
                allChildHeight += getChildAt(index).measuredHeight + getChildAt(index).marginTop + getChildAt(
                    index
                ).marginBottom

                // 最后一条的时候 加上当前view自身的padding
                if (index == childCount - 1) {
                    allChildHeight += this.paddingTop + this.paddingBottom
                }
            }

            // 根据是否展开设置高度
            if (isOpen) {
                setMeasuredDimension(
                    widthMeasureSpec,
                    firstChildHeight + ((allChildHeight - firstChildHeight) * animPercent).toInt()
                )
            } else {
                setMeasuredDimension(
                    widthMeasureSpec,
                    allChildHeight - ((allChildHeight - firstChildHeight) * animPercent).toInt()
                )
            }
        }
    }

    fun toggle(): Boolean {
        isOpen = !isOpen
        startAnim()
        return isOpen
    }

    /**
     * 执行动画的时候 更改 animPercent 属性的值 即从0-1
     */
    @SuppressLint("AnimatorKeep", "ObjectAnimatorBinding")
    private fun startAnim() {
        // ofFloat，of xxxX 根据参数类型来确定
        // 1，动画对象，即当前view。2.动画属性名。3，起始值。4，目标值。
        val animator = ObjectAnimator.ofFloat(this, "animPercent", 0f, 1f)
        animator.duration = 500
        animator.start()
    }

}
