package top.iqqcode.module.components.expend.spread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import top.iqqcode.module.components.R
import top.iqqcode.module.components.databinding.ActivitySpreadBinding

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