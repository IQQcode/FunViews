package top.iqqcode.module.layout.mark

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import top.iqqcode.module.layout.R
import top.iqqcode.module.layout.databinding.ActivityMarkBinding

/**
 * 添加水印
 *
 * @constructor Create empty Mark activity
 */
class MarkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMarkBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}