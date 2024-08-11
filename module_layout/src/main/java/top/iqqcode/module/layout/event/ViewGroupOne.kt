package top.iqqcode.module.layout.event

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout


class ViewGroupOne(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "ViewGroup1 dispatchTouchEvent return super")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "ViewGroup1 onTouchEvent return super")
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "ViewGroup1 onInterceptTouchEvent return super")
        return super.onInterceptTouchEvent(ev)
    }

}