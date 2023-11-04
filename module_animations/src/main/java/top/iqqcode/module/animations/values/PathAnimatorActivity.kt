package top.iqqcode.module.animations.values

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.funviews.base.ReceiveOpt
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.animations.databinding.ActivityMainAnimationBinding
import top.iqqcode.module.animations.databinding.ActivityPathAnimatorBinding


/**
 * 属性动画基础使用
 *
 * @constructor Create empty Point animator activity
 */
@Route(path = RouterAbility.ANIMATIONS_PATH_ANIMATOR)
class PathAnimatorActivity : AppCompatActivity(), ReceiveOpt {

    private lateinit var binding: ActivityPathAnimatorBinding
    // private lateinit var mOptFragment: OptFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()
    }

    override fun initial() {
//        mOptFragment = FragmentExt.createFragment(R.layout.fragment_path)
//        FragmentExt.addFragment(
//            this.supportFragmentManager,
//            R.id.optFragmentContainer,
//            mOptFragment
//        )
    }

}
