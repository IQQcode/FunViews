package top.iqqcode.module.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.layout.databinding.ActivityLayoutBinding
import top.iqqcode.module.layout.databinding.ActivityPrintBinding
import top.iqqcode.module.layout.mark.MarkActivity
import top.iqqcode.module.layout.print.PrintActivity


@Route(path = RouterAbility.LAYOUT_MAIN)
class LayoutActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.cardView01.setOnClickListener(this)
        binding.cardView02.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardView01 -> {
                startActivity(
                    Intent(
                        this@LayoutActivity,
                        MarkActivity::class.java
                    )
                )
            }

            R.id.cardView02 -> {
                startActivity(
                    Intent(
                        this@LayoutActivity,
                        PrintActivity::class.java
                    )
                )
            }
        }
    }
}