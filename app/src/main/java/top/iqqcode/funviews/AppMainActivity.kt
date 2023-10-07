package top.iqqcode.funviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.edittext.CustomTextActivity
import top.iqqcode.module.animations.values.PathAnimatorActivity
import top.iqqcode.module.components.expend.hotspot.ExpendClickActivity
import top.iqqcode.module.components.expend.spread.SpreadActivity
import top.iqqcode.module.layout.print.PrintActivity
import top.iqqcode.funviews.R
import top.iqqcode.funviews.databinding.ActivityAppMainBinding

class AppMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAppMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        binding.customEditButton.setOnClickListener(this)
        binding.printButton.setOnClickListener(this)
        binding.expendButton.setOnClickListener(this)
        binding.groupEntryButton.setOnClickListener(this)
        binding.floatExpendButton.setOnClickListener(this)
        binding.floatExpendBaseDemo.setOnClickListener(this)
        binding.frsFloatExpendDemo.setOnClickListener(this)
        binding.carouselFloat.setOnClickListener(this)
        binding.naturalAlphaDemo.setOnClickListener(this)
        binding.iconSizeExpend.setOnClickListener(this)
        binding.emotionLabel.setOnClickListener(this)
        binding.eggFalling.setOnClickListener(this)
        binding.pathAnimator.setOnClickListener(this)
        binding.spreadButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.customEditButton -> startActivity(
                Intent(
                    this@AppMainActivity,
                    CustomTextActivity::class.java
                ).setClassName(
                    this@AppMainActivity,
                    "top.iqqcode.module.components.edittext.CustomTextActivity"
                )
            )

            R.id.printButton -> startActivity(Intent(this@AppMainActivity, PrintActivity::class.java))
            R.id.expendButton -> startActivity(
                Intent(
                    this@AppMainActivity,
                    ExpendClickActivity::class.java
                )
            )

//            R.id.groupEntryButton -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    IMGroupEntryActivity::class.java
//                )
//            )
//
//            R.id.floatExpendButton -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    FloatExpandActivity::class.java
//                )
//            )
//
//            R.id.floatExpendBaseDemo -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    ExpendBaseDemoActivity::class.java
//                )
//            )
//
//            R.id.frsFloatExpendDemo -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    FrsFloatEntranceActivity::class.java
//                )
//            )
//
//            R.id.carouselFloat -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    CarouselDemoActivity::class.java
//                )
//            )
//
//            R.id.naturalAlphaDemo -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    NaturalAlphaActivity::class.java
//                )
//            )
//
//            R.id.iconSizeExpend -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    ExpendBoxActivity::class.java
//                )
//            )
//
//            R.id.emotionLabel -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    EmotionLabelActivity::class.java
//                )
//            )

            R.id.eggFalling -> {
                // startActivity(Intent(this@MainActivity, FallingActivity::class.java))
                // 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                ARouter.getInstance().build(RouterAbility.CORE_MAIN_FRAME).navigation()
            }

            R.id.pathAnimator -> startActivity(
                Intent(
                    this@AppMainActivity,
                    PathAnimatorActivity::class.java
                )
            )

            R.id.spreadButton -> startActivity(
                Intent(
                    this@AppMainActivity,
                    SpreadActivity::class.java
                )
            )
        }
    }
}