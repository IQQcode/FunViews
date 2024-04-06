package top.iqqcode.funviews

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import top.iqqcode.funviews.databinding.ActivityAppMainBinding

class AppMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAppMainBinding

    private lateinit var mFragmentTitles: Array<String>
    private lateinit var mFragmentPaths: Array<String>
    private lateinit var mFragments: Array<Fragment?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFragmentTitles = resources.getStringArray(R.array.fragments)
        mFragmentPaths = resources.getStringArray(R.array.fragments_full_path)
        mFragments = arrayOfNulls(mFragmentTitles.size)

        binding.viewPager.setAdapter(object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return this@AppMainActivity.createFragment(position) ?: Fragment()
            }

            override fun getItemCount(): Int {
                return mFragmentTitles.size
            }
        })

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setText(
                mFragmentTitles[position]
                    .replace("SampleFragment", "")
                    .replace("[A-Z]".toRegex(), " $0")
            )
        }.attach()
    }

    override fun onClick(v: View?) {

    }

    private fun createFragment(index: Int): Fragment? {
        if (mFragments[index] != null) {
            return mFragments[index]
        }
        val name: String = mFragmentPaths[index]
        var fragment: Fragment? = null
        try {
            fragment = Class.forName(name).newInstance() as Fragment
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        if (fragment != null) {
            mFragments[index] = fragment
        }
        return mFragments[index]
    }
}