package top.iqqcode.module.layout.event

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout


class ViewExtension(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "[View] dispatchTouchEvent return super")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("TouchEventTest", "[View] onTouchEvent return super")
        return super.onTouchEvent(event)
    }


}