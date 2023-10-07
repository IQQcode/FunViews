package top.iqqcode.module.animations.values

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.funviews.base.ReceiveOpt
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.animations.R
import top.iqqcode.module.animations.databinding.ActivityPointAnimatorBinding
import top.iqqcode.module.core.menu.OnMenuSelectedListener
import top.iqqcode.module.core.menu.OnMenuStatusChangeListener


/**
 * 属性动画基础使用
 *
 * @constructor Create empty Point animator activity
 */
@Route(path = RouterAbility.ANIMATIONS_PATH_ANIMATOR)
class PathAnimatorActivity : AppCompatActivity(), ReceiveOpt {

    private lateinit var binding: ActivityPointAnimatorBinding
    // private lateinit var mOptFragment: OptFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()

        binding.circleMenu.setMainMenu(
            Color.parseColor("#CDCDCD"),
            R.mipmap.icon_home,
            R.mipmap.icon_home
        )
            .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home)
            .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_home)
            .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_home)
            .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_home)
            .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_home)
            .setOnMenuSelectedListener(OnMenuSelectedListener { })
            .setOnMenuStatusChangeListener(object : OnMenuStatusChangeListener {
                override fun onMenuOpened() {}
                override fun onMenuClosed() {}
            })
    }

    override fun initial() {
//        mOptFragment = FragmentExt.createFragment(R.layout.fragment_path)
//        FragmentExt.addFragment(
//            this.supportFragmentManager,
//            R.id.optFragmentContainer,
//            mOptFragment
//        )
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        // binding.circleMenu.openMenu()
        return super.onMenuOpened(featureId, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // binding.circleMenu.closeMenu()
    }

}
