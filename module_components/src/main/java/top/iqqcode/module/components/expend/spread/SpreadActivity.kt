package top.iqqcode.module.components.expend.spread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.ComponentsRouter
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.ActivitySpreadBinding

@Route(path = ComponentsRouter.SPREAD_ACTIVITY)
class SpreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpreadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.spreadFragmentContainer).navigateUp()
}