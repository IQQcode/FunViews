package top.iqqcode.lib.common.util

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths

object JSONConfigRender {

    @JvmStatic
    fun readJsonFromAssets(context: Context, fileName: String): String {
        val assetManager: AssetManager = context.assets
        val stringBuilder = StringBuilder()
        try {
            val inputStream = assetManager.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }
}


