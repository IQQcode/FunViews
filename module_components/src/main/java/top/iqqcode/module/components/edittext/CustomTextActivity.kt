package top.iqqcode.module.components.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.databinding.ActivityCustomTextBinding

@Route(path = RouterAbility.COMPONENTS_CUSTOM_TEXT)
class CustomTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}