package top.iqqcode.module.animations.values

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.iqqcode.module.animations.R
import top.iqqcode.module.animations.databinding.ActivityPointAnimatorBinding
import top.iqqcode.funviews.base.FragmentExt
import top.iqqcode.funviews.base.OptFragment
import top.iqqcode.funviews.base.ReceiveOpt


/**
 * 属性动画基础使用
 *
 * @constructor Create empty Point animator activity
 */
class PathAnimatorActivity : AppCompatActivity(), ReceiveOpt {

    private lateinit var binding: ActivityPointAnimatorBinding
    private lateinit var mOptFragment: OptFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()
    }

    override fun initial() {
        mOptFragment = FragmentExt.createFragment(R.layout.fragment_path)
        FragmentExt.addFragment(
            this.supportFragmentManager,
            R.id.optFragmentContainer,
            mOptFragment
        )
    }

}
