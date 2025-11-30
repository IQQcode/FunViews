package top.iqqcode.module.components.docks

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.FrameworksRouter
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.ActivityComponentsBinding
import top.iqqcode.module.components.databinding.ActivityDockBarBinding

@Route(path = RouterAbility.COMPONENTS_DOCK_BAR)
class DockBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDockBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDockBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {

    }
}