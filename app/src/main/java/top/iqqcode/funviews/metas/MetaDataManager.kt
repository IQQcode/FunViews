package top.iqqcode.funviews.metas

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Matcher
import java.util.regex.Pattern


class MetaDataManager(private val activity: AppCompatActivity) {

    companion object {
        private const val DEBUG_APK_UPGRADE_KIT = "DEBUG_APK_UPGRADE_KIT"
    }

    fun getApkInfo() {
        try {
            // 获取应用程序包的上下文
            val context: Context = activity
            // 获取应用程序的包管理器
            val packageManager = context.packageManager
            // 获取应用程序的组件信息，如 Activity、Service、Receiver 等
            val componentName = ComponentName(context, activity::class.java)
            // 获取 AndroidManifest.xml 中的 ApplicationInfo 对象
            val applicationInfo = packageManager.getApplicationInfo(
                componentName.packageName,
                PackageManager.GET_META_DATA
            )

            // 获取 AndroidManifest.xml 中 <meta-data> 下的 key-value 数据
            val metaData = applicationInfo.metaData
            if (metaData != null) {
                // 替换 "your_key" 为你在 AndroidManifest.xml 中设置的 key
                val value = metaData.getString(DEBUG_APK_UPGRADE_KIT)
                val metas = value?.let { parseMetaData(it) }
                if (metas != null && metas.size == 2) {
                    // 使用获取到的 value 数据
                    Log.i("JIAZIHUI", "getApkInfo branch = ${metas[0]} | apkID = ${metas[1]}")
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun parseMetaData(values: String): List<String> {
        val metas = arrayListOf<String>()
        // 定义正则表达式匹配模式
        val pattern: Pattern = Pattern.compile("&\\{([^}]*)\\}")
        val matcher: Matcher = pattern.matcher(values)
        // 如果找到匹配的内容
        while (matcher.find()) {
            // 提取匹配到的内容
            val res = matcher.group(1) // 获取第一个匹配项中的字母
            if (res != null) {
                metas.add(res)
            }

        }
        return metas
    }
}