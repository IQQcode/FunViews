package top.iqqcode.module.layout.mark.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi

object DrawableUtils {

    @JvmStatic
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun contextDrawableToBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = context.resources.getDrawable(drawableId, null)
        return drawableToBitmap(drawable)
    }
}
