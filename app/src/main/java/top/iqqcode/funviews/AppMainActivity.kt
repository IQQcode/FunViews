package top.iqqcode.funviews

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.funviews.databinding.ActivityAppMainBinding
import top.iqqcode.lib.common.util.UtilHelper
import top.iqqcode.module.core.weight.menu.OnMenuSelectedListener
import top.iqqcode.module.core.weight.menu.OnMenuStatusChangeListener
import top.iqqcode.module.core.weight.menu.SubMenuOptions

/**
 * 主工程页面
 *
 * @constructor Create empty App main activity
 */
class AppMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAppMainBinding

    private var sensorManager: SensorManager? = null
    private var defaultSensor: Sensor? = null

    private val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1] * 2.0f
                binding.badgeView.getmMobike().onSensorChanged(-x, y)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    // 重力墙icon
    private val imgArgs = intArrayOf(
        R.mipmap.share_wechat,
        R.mipmap.share_wechat,
        R.mipmap.share_wechat,
        R.mipmap.share_wechat,
        R.mipmap.share_wechat,
        R.mipmap.share_wechat,
        R.mipmap.share_wechat
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initCircleMenu()
        initBadges()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        defaultSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // initClick()
    }

    private fun initView() {
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.badgeView.setOnClickListener(this)
    }

    private fun initBadges() {
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        for (i in imgArgs.indices) {
            val imageView = ImageView(this)
            imageView.setImageResource(imgArgs[i])
            imageView.setTag(top.iqqcode.module.core.R.id.mobike_view_circle_tag, true)
            binding.badgeView.addView(imageView, layoutParams)
        }
    }

    /** 初始化菜单动效 */
    private fun initCircleMenu() {
        binding.circleMenu.setMainMenu(
            Color.parseColor("#CDCDCD"),
            R.mipmap.icon_menu,
            R.mipmap.icon_cancel
        )
            .addSubMenu(
                Color.parseColor("#258CFF"),
                R.mipmap.icon_home,
                SubMenuOptions.MODULE_CORE_MAIN
            )
            .addSubMenu(
                Color.parseColor("#30A400"),
                R.mipmap.icon_search,
                SubMenuOptions.MODULE_LAYOUT
            )
            .addSubMenu(
                Color.parseColor("#FF4B32"),
                R.mipmap.icon_notify,
                SubMenuOptions.MODULE_COMPONENTS
            )
            .addSubMenu(
                Color.parseColor("#8A39FF"),
                R.mipmap.icon_setting,
                SubMenuOptions.MODULE_ANIMATIONS
            )
            .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_gps, SubMenuOptions.MODULE_NON)
            .setOnMenuSelectedListener(OnMenuSelectedListener {
                Log.i("JIAZIHUI", "initCircleMenu: $it")
                when (it) {
                    SubMenuOptions.MODULE_CORE_MAIN -> {
                        // 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                        ARouter.getInstance().build(RouterAbility.CORE_MAIN_FRAME).navigation()
                    }

                    SubMenuOptions.MODULE_LAYOUT -> {
                        ARouter.getInstance().build(RouterAbility.LAYOUT_MAIN).navigation()
                    }

                    SubMenuOptions.MODULE_COMPONENTS -> {
                        ARouter.getInstance().build(RouterAbility.COMPONENTS_MAIN).navigation()
                    }

                    SubMenuOptions.MODULE_ANIMATIONS -> {
                        ARouter.getInstance().build(RouterAbility.ANIMATIONS_MAIN).navigation()
                    }

                    else -> {

                    }
                }
            })
            .setOnMenuStatusChangeListener(object : OnMenuStatusChangeListener {
                override fun onMenuOpened() {

                }

                override fun onMenuClosed() {

                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        binding.circleMenu.openMenu()
        return super.onMenuOpened(featureId, menu)
    }

    override fun onStart() {
        super.onStart()
        binding.badgeView.getmMobike()?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.badgeView.getmMobike()?.onStop()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            mSensorListener,
            defaultSensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(mSensorListener)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.badgeView -> binding.circleMenu.closeMenu()
        }
    }

//    private fun initClick() {
//        binding.customEditButton.setOnClickListener(this)
//        binding.printButton.setOnClickListener(this)
//        binding.expendButton.setOnClickListener(this)
//        binding.groupEntryButton.setOnClickListener(this)
//        binding.floatExpendButton.setOnClickListener(this)
//        binding.floatExpendBaseDemo.setOnClickListener(this)
//        binding.frsFloatExpendDemo.setOnClickListener(this)
//        binding.carouselFloat.setOnClickListener(this)
//        binding.naturalAlphaDemo.setOnClickListener(this)
//        binding.iconSizeExpend.setOnClickListener(this)
//        binding.emotionLabel.setOnClickListener(this)
//        binding.eggFalling.setOnClickListener(this)
//        binding.pathAnimator.setOnClickListener(this)
//        binding.spreadButton.setOnClickListener(this)
//    }

//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.customEditButton -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    CustomTextActivity::class.java
//                ).setClassName(
//                    this@AppMainActivity,
//                    "top.iqqcode.module.components.edittext.CustomTextActivity"
//                )
//            )
//
//            R.id.printButton -> startActivity(Intent(this@AppMainActivity, PrintActivity::class.java))
//            R.id.expendButton -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    ExpendClickActivity::class.java
//                )
//            )
//
////            R.id.groupEntryButton -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    IMGroupEntryActivity::class.java
////                )
////            )
////
////            R.id.floatExpendButton -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    FloatExpandActivity::class.java
////                )
////            )
////
////            R.id.floatExpendBaseDemo -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    ExpendBaseDemoActivity::class.java
////                )
////            )
////
////            R.id.frsFloatExpendDemo -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    FrsFloatEntranceActivity::class.java
////                )
////            )
////
////            R.id.carouselFloat -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    CarouselDemoActivity::class.java
////                )
////            )
////
////            R.id.naturalAlphaDemo -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    NaturalAlphaActivity::class.java
////                )
////            )
////
////            R.id.iconSizeExpend -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    ExpendBoxActivity::class.java
////                )
////            )
////
////            R.id.emotionLabel -> startActivity(
////                Intent(
////                    this@AppMainActivity,
////                    EmotionLabelActivity::class.java
////                )
////            )
//
//            R.id.eggFalling -> {
//                // startActivity(Intent(this@MainActivity, FallingActivity::class.java))
//                // 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//                ARouter.getInstance().build(RouterAbility.CORE_MAIN_FRAME).navigation()
//            }
//
//            R.id.pathAnimator -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    PathAnimatorActivity::class.java
//                )
//            )
//
//            R.id.spreadButton -> startActivity(
//                Intent(
//                    this@AppMainActivity,
//                    SpreadActivity::class.java
//                )
//            )
//
//
//
//        }
//    }
}