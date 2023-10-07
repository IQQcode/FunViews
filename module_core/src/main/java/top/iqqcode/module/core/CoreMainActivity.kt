package top.iqqcode.module.core

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.core.databinding.ActivityCoreMainBinding
import top.iqqcode.module.core.menu.OnMenuSelectedListener
import top.iqqcode.module.core.menu.OnMenuStatusChangeListener


/** 工程项目UI框架 */
@Route(path = RouterAbility.CORE_MAIN_FRAME)
class CoreMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCoreMainBinding

    private var sensorManager: SensorManager? = null
    private var defaultSensor: Sensor? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoreMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initCircleMenu()
        initBadges()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        defaultSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
            imageView.setTag(R.id.mobike_view_circle_tag, true)
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
            .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home)
            .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_search)
            .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
            .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_setting)
            .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_gps)
            .setOnMenuSelectedListener(OnMenuSelectedListener {
                Log.i("JIAZIHUI", "initCircleMenu: $it")
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
        when(view?.id) {
            R.id.badgeView -> binding.circleMenu.closeMenu()
        }
    }
}