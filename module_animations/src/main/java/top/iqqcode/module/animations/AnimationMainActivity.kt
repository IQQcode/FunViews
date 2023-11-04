package top.iqqcode.module.animations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.animations.databinding.ActivityMainAnimationBinding

@Route(path = RouterAbility.ANIMATIONS_MAIN)
class AnimationMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}