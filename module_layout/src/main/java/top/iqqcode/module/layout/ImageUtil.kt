package top.iqqcode.module.layout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect


object ImageUtil {

    /**
     * 设置水印图片在左上角
     *
     * @param context 上下文
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    fun createWaterMaskLeftTop(
        context: Context,
        src: Bitmap?,
        watermark: Bitmap,
        paddingLeft: Int,
        paddingTop: Int
    ): Bitmap? {
        return createWaterMaskBitmap(
            src, watermark,
            dp2px(context, paddingLeft.toFloat()), dp2px(context, paddingTop.toFloat())
        )
    }

    private fun createWaterMaskBitmap(
        src: Bitmap?,
        watermark: Bitmap,
        paddingLeft: Int,
        paddingTop: Int
    ): Bitmap? {
        if (src == null) {
            return null
        }
        val width = src.getWidth()
        val height = src.getHeight()
        //创建一个bitmap
        val newb =
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        val canvas = Canvas(newb)
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0f, 0f, null)
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft.toFloat(), paddingTop.toFloat(), null)
        // 保存
        canvas.save() // Canvas.ALL_SAVE_FLAG
        // 存储
        canvas.restore()
        return newb
    }

    /**
     * 设置水印图片在右下角
     *
     * @param context 上下文
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    fun createWaterMaskRightBottom(
        context: Context,
        src: Bitmap,
        watermark: Bitmap,
        paddingRight: Int,
        paddingBottom: Int
    ): Bitmap? {
        return createWaterMaskBitmap(
            src, watermark,
            src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight.toFloat()),
            src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom.toFloat())
        )
    }

    /**
     * 设置水印图片到右上角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    fun createWaterMaskRightTop(
        context: Context,
        src: Bitmap,
        watermark: Bitmap,
        paddingRight: Int,
        paddingTop: Int
    ): Bitmap? {
        return createWaterMaskBitmap(
            src, watermark,
            src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight.toFloat()),
            dp2px(context, paddingTop.toFloat())
        )
    }

    /**
     * 设置水印图片到左下角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    fun createWaterMaskLeftBottom(
        context: Context,
        src: Bitmap,
        watermark: Bitmap,
        paddingLeft: Int,
        paddingBottom: Int
    ): Bitmap? {
        return createWaterMaskBitmap(
            src, watermark, dp2px(context, paddingLeft.toFloat()),
            src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom.toFloat())
        )
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    fun createWaterMaskCenter(src: Bitmap, watermark: Bitmap): Bitmap? {
        return createWaterMaskBitmap(
            src, watermark,
            (src.getWidth() - watermark.getWidth()) / 2,
            (src.getHeight() - watermark.getHeight()) / 2
        )
    }

    /**
     * 给图片添加文字到左上角
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    fun drawTextToLeftTop(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int,
        color: Int,
        paddingLeft: Int,
        paddingTop: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(color)
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            context, bitmap, text, paint, bounds,
            dp2px(context, paddingLeft.toFloat()),
            dp2px(context, paddingTop.toFloat()) + bounds.height()
        )
    }

    /**
     * 绘制文字到右下角
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    fun drawTextToRightBottom(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int,
        color: Int,
        paddingRight: Int,
        paddingBottom: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(color)
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            context, bitmap, text, paint, bounds,
            bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight.toFloat()),
            bitmap.getHeight() - dp2px(context, paddingBottom.toFloat())
        )
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    fun drawTextToRightTop(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int,
        color: Int,
        paddingRight: Int,
        paddingTop: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(color)
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            context, bitmap, text, paint, bounds,
            bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight.toFloat()),
            dp2px(context, paddingTop.toFloat()) + bounds.height()
        )
    }

    /**
     * 绘制文字到左下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    fun drawTextToLeftBottom(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int,
        color: Int,
        paddingLeft: Int,
        paddingBottom: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(color)
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            context, bitmap, text, paint, bounds,
            dp2px(context, paddingLeft.toFloat()),
            bitmap.getHeight() - dp2px(context, paddingBottom.toFloat())
        )
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    fun drawTextToCenter(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int,
        color: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(color)
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            context, bitmap, text, paint, bounds,
            (bitmap.getWidth() - bounds.width()) / 2,
            (bitmap.getHeight() + bounds.height()) / 2
        )
    }

    //图片上绘制文字
    private fun drawTextToBitmap(
        context: Context,
        bitmap: Bitmap,
        text: String,
        paint: Paint,
        bounds: Rect,
        paddingLeft: Int,
        paddingTop: Int
    ): Bitmap {
        var bitmap = bitmap
        var bitmapConfig = bitmap.getConfig()
        paint.isDither = true // 获取跟清晰的图像采样
        paint.isFilterBitmap = true // 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
        return bitmap
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    fun scaleWithWH(src: Bitmap?, w: Double, h: Double): Bitmap? {
        return if (w == 0.0 || h == 0.0 || src == null) {
            src
        } else {
            // 记录src的宽高
            val width = src.getWidth()
            val height = src.getHeight()
            // 创建一个matrix容器
            val matrix = Matrix()
            // 计算缩放比例
            val scaleWidth = (w / width).toFloat()
            val scaleHeight = (h / height).toFloat()
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight)
            // 创建缩放后的图片
            Bitmap.createBitmap(src, 0, 0, width, height, matrix, true)
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}


