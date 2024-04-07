package top.iqqcode.module.components.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.didichuxing.doraemonkit.widget.brvah.entity.node.BaseNode
import top.iqqcode.lib.common.router.ComponentsRouter
import top.iqqcode.module.components.R

/**
 * [公众号]：Android kotlin用RecyclerView(androidx+BRVAH)实现分组/吸顶/可展开收起二级列表功能
 *
 * @constructor Create empty Table sub list activity
 */
@Route(path = ComponentsRouter.TABLE_SUB_LIST_ACTIVITY)
class TableSubListActivity : AppCompatActivity() {

    //listType = 1，1表示item子菜单类似于ListView
    private var listType: Int = 1

    private var list = mutableListOf<BaseNode>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_sub_list)

    }
}