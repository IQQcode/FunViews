package top.iqqcode.module.components.expend.spread

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.FragmentSpreadLayoutBinding
import top.iqqcode.funviews.base.BaseFragment

/**
 * @Author: jiazihui
 * @Date: 2023-04-09 16:11
 * @Description:可折叠布局
 * @see:https://blog.csdn.net/yechaoa/article/details/113847705
 */
class SpreadLayoutFragment : BaseFragment() {

    private lateinit var binding: FragmentSpreadLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSpreadLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changeNextLayout.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_to_spreadGroupFragment)
        }

        binding.expendButton.setOnClickListener {
            val toggle = binding.expandLinearLayout.toggle()
            binding.textTip.text = if (toggle) "收起" else "展开"
            startImageRotate(binding.ivArrow, toggle)
        }
    }

    /**
     * 旋转箭头图标
     */
    private fun startImageRotate(imageView: ImageView, toggle: Boolean) {
        val tarRotate: Float = if (toggle) {
            0f
        } else {
            180f
        }

        imageView.apply {
            ObjectAnimator.ofFloat(this, "rotation", rotation, tarRotate).let {
                it.duration = 300
                it.start()
            }
        }
    }
}
