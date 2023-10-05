package top.iqqcode.module.components.expend.spread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.FragmentSpreadGroupBinding
import top.iqqcode.funviews.base.BaseFragment

/**
 * @Author: jiazihui
 * @Date: 2023-04-09 17:09
 * @Description:
 */
class SpreadGroupFragment : BaseFragment() {

    private lateinit var binding: FragmentSpreadGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSpreadGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changeGroupLayout.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_to_spreadLayoutFragment)
        }
    }
}