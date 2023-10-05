package top.iqqcode.module.layout.print

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import top.iqqcode.module.layout.databinding.ActivityPrintBinding

class PrintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrintBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(binding.printView)
    }
}