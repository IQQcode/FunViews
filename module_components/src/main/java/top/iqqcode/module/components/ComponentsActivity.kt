package top.iqqcode.module.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.components.databinding.ActivityComponentsBinding
import top.iqqcode.module.components.expend.spread.SpreadActivity
import top.iqqcode.module.components.list.TableSubListActivity

@Route(path = RouterAbility.COMPONENTS_MAIN)
class ComponentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComponentsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComponentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.tableSubList.setOnClickListener {
            startActivity(
                Intent(
                    this@ComponentsActivity,
                    TableSubListActivity::class.java
                )
            )
        }
    }
}