package top.iqqcode.module.layout.event

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import top.iqqcode.module.layout.databinding.ActivityDemo01EventBinding

/**
 * 事件分发学习
 *
 * [Android斩首行动—滑动冲突](https://juejin.cn/post/7168445102984003591?from=search-suggest)
 */
class Demo01EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemo01EventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemo01EventBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "Activity dispatchTouchEvent return super")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "Activity onTouchEvent return super")
        return super.onTouchEvent(event)
    }

}