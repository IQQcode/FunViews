package top.iqqcode.lib.common.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager


/**
 * @Author: jiazihui
 * @Date: 2022-10-22 22:01
 * @Description:
 */
object UtilHelper {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getDimens(context: Context, dimenId: Int): Int {
        return context.resources.getDimensionPixelSize(dimenId)
    }

    /**
     * 获取应用的屏幕参数DisplayMetrics
     * DisplayMetrics还可以获取一些物理像素、比例之类的数据
     */
    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    /**
     * 获取应用屏幕的宽度
     */
    fun getApplicationScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    /**
     * 获取应用屏幕的高度
     */
    fun getApplicationScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    /**
     * 获取实际的尺寸，不包括一些占用屏幕的虚拟按键、水滴、刘海
     */
    private fun getRealPoint(context: Context): Point {
        // 获取屏幕管理器 Application没有这个属性,但activity自带这个属性
        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        // 获取默认视图 defaultDisplay已经弃用
        val defaultDisplay = windowManager.defaultDisplay
        // new一个Point 实际是通过对象引用的方式获取宽高
        val point = Point()
        // 获取实际宽高，数据储存在point中 getRealSize已经弃用
        defaultDisplay.getSize(point)
        return point
    }

    /**
     * 获取实际的宽 不包括一些占用屏幕的虚拟按键、水滴、刘海
     */
    fun getEquipmentWidth(context: Context): Int {
        return getRealPoint(context).x
    }

    /**
     * 获取实际的高 不包括一些占用屏幕的虚拟按键、水滴、刘海
     */
    fun getEquipmentHeight(context: Context): Int {
        return getRealPoint(context).y
    }

}