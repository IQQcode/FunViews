package top.iqqcode.module.animations.values

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import top.iqqcode.module.animations.databinding.FragmentPathBinding

/**
 * @Author: jiazihui
 * @Date: 2023-02-26 22:09
 * @Description:
 */
class PathFragment : Fragment() {

    private lateinit var binding: FragmentPathBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPathBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
    }

}