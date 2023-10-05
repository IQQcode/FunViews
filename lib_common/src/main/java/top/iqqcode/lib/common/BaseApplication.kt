package top.iqqcode.lib.common

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.didichuxing.doraemonkit.DoKit
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp

/**
 * @author jiazihui
 */
@HiltAndroidApp
open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (true) {    // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        initFresco()
        initDoKit()
    }

    private fun initFresco() {
        Fresco.initialize(this)
    }

    private fun initDoKit() {
        DoKit.Builder(this)
            .productId("需要使用平台功能的话，需要到dokit.cn平台申请id")
            .build()
    }
}
