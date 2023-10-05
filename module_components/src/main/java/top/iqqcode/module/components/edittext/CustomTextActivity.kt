package top.iqqcode.module.components.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import top.iqqcode.module.components.databinding.ActivityCustomTextBinding

class CustomTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}