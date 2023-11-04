package top.iqqcode.module.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.alibaba.android.arouter.facade.annotation.Route
import top.iqqcode.lib.common.router.RouterAbility
import top.iqqcode.module.core.data.AutoDetailData
import top.iqqcode.module.core.databinding.ActivityCoreMainBinding
import top.iqqcode.module.core.weight.list.CommonAdapter


/**
 * 工程项目UI框架
 *
 * @constructor Create empty Core main activity
 */
@Route(path = RouterAbility.CORE_MAIN_FRAME)
class CoreMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCoreMainBinding
    private lateinit var mAdapter: CommonAdapter

    private val dataList: MutableList<AutoDetailData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoreMainBinding.inflate(layoutInflater)
        requestWindowFeature(1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        setContentView(binding.root)

        initData()
        initView()
    }

    private fun initView() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerCoreView.layoutManager = layoutManager
        mAdapter = CommonAdapter(this, 1, 1)
        mAdapter.setData(dataList)
        binding.recyclerCoreView.adapter = mAdapter

        // 真正处理item点击事件
        mAdapter.setOnItemClickListener(object : CommonAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //ARouter.getInstance().build(RouterAbility.LAYOUT_MAIN).navigation()
            }
        })

        // 这里注意一点，如果你的RecyclerView使用Grid类型列表在设置Adapter后需要调用这个方法，
        // 根据当前Item类型来判断占据的横向格数，这也是Adapter里面实现isHeaderView和isBottomView的缘故
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mAdapter.isHeaderView(position) || mAdapter.isBottomView(position)) layoutManager.spanCount else 1
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

        }
    }

    private fun initData() {
        for (i in 0..20) {
            dataList.add(
                AutoDetailData(
                    "Transport",
                    "彩蛋雨动画哈哈哈哈",
                    top.iqqcode.lib.common.R.drawable.bills_img_background,
                    top.iqqcode.lib.common.R.drawable.invoice
                )
            )
        }
    }
}