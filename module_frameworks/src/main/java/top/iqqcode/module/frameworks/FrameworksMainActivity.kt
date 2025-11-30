package top.iqqcode.module.frameworks

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.FrameworksRouter
import top.iqqcode.module.frameworks.databinding.ActivityFrameworksBinding

@Route(path = FrameworksRouter.HANDLERS_WORKS_ACTIVITY)
class FrameworksMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrameworksBinding

    // 创建主线程Handler
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_PROGRESS -> {
                    // 更新进度条
                    val progress = msg.arg1
                    binding.progressBar.progress = progress
                }

                MSG_TASK_COMPLETE -> {
                    // 任务完成后的UI操作
                    binding.resultTextView.text = "任务完成"
                    binding.progressBar.progress = 100
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFrameworksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 启动后台线程执行耗时任务
        Thread {
            // 模拟耗时操作
            for (i in 1..100) {
                // 模拟工作
                Thread.sleep(50)

                // 发送进度更新消息
                val msg = handler.obtainMessage(MSG_UPDATE_PROGRESS)
                msg.arg1 = i

                // 推荐的写法：从对象池复用 Message
                val msg2 = Message.obtain() // 从池里取一个空闲的Message
                msg2.what = MSG_UPDATE_PROGRESS
                msg2.obj = "这是通过 Message.obtain() 发送的数据"

                handler.sendMessage(msg)
            }

            // 发送任务完成消息
            handler.sendEmptyMessage(MSG_TASK_COMPLETE)
        }.start()
    }

    override fun onDestroy() {
        // 移除所有消息
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    // 定义消息类型常量
    companion object {
        const val MSG_UPDATE_PROGRESS = 1
        const val MSG_TASK_COMPLETE = 2
    }
}