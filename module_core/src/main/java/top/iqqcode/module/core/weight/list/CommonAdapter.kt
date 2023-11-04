package top.iqqcode.module.core.weight.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import top.iqqcode.module.core.R
import top.iqqcode.module.core.data.AutoDetailData

/**
 * RecyclerView添加头部和尾部 https://www.jianshu.com/p/e9dfb7167f87
 *
 * @constructor Create empty Common adapter
 * @property mContext
 * @property headerCount
 * @property bottomCount
 */
class CommonAdapter(
    private val mContext: Context,
    private val headerCount: Int, // 头部View个数
    private val bottomCount: Int // 底部View个数
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<AutoDetailData>? = null
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    // 声明回调接口
    private var onClickListener: OnItemClickListener? = null

    fun setData(list: List<AutoDetailData>?) {
        mList = list
    }

    /**
     * 设置回调接口
     *
     * @param clickListener OnItemClickListener?
     */
    fun setOnItemClickListener(clickListener: OnItemClickListener?) {
        onClickListener = clickListener
    }

    /**
     * 创建ViewHolder实例
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                HeaderViewHolder(inflater.inflate(R.layout.recycler_list_header, parent, false))
            }
            ITEM_TYPE_BOTTOM -> {
                FooterViewHolder(inflater.inflate(R.layout.recycler_list_footer, parent, false))
            }
            else -> {
                ViewHolder(inflater.inflate(R.layout.material_card_view, parent, false));
            }
        }
    }

    /**
     * 对RecyclerView子项的数据进行赋值的，会在每个子项被滚动到屏幕内的时候执行
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.headerTitle.text = "Classify Transaction"
            }

            is FooterViewHolder -> {

            }

            is ViewHolder -> {
                val itemData = mList?.getOrNull(position - headerCount)
                with(holder) {
                    titleView.text = itemData?.title
                    contentView.text = itemData?.content
                    itemData?.iconBg?.let { elementIcon.setBackgroundResource(it) }
                    itemData?.portrait?.let { elementIcon.setImageResource(it) }

                    // 通过为条目设置点击事件触发回调
                    itemView.setOnClickListener {
                        Toast.makeText(mContext, "item click", Toast.LENGTH_SHORT).show()
                        onClickListener?.onItemClick(adapterPosition)
                    }
                }
            }
        }
    }

    override fun getItemCount() = if (mList == null) 0 else mList!!.size + headerCount + bottomCount

    private fun getDataCount() = if (mList == null) 0 else mList!!.size

    /**
     * 判断当前item是否是HeadView
     *
     * @param position
     * @return
     */
    fun isHeaderView(position: Int): Boolean {
        return headerCount != 0 && position < headerCount
    }

    /**
     * 判断当前item是否是FooterView
     *
     * @param position
     * @return
     */
    fun isBottomView(position: Int): Boolean {
        return bottomCount != 0 && position >= (headerCount + getDataCount())
    }


    /**
     * 判断当前item类型
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (headerCount != 0 && position < headerCount) {
            // 头部View
            ITEM_TYPE_HEADER
        } else if (bottomCount != 0 && (position >= headerCount + getDataCount())) {
            // 底部View
            ITEM_TYPE_BOTTOM
        } else {
            // 内容View
            ITEM_TYPE_CONTENT
        }
    }


    // 定义回调接口
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        // item类型
        const val ITEM_TYPE_HEADER = 0 // 头部
        const val ITEM_TYPE_CONTENT = 1  // 内容
        const val ITEM_TYPE_BOTTOM = 2 // 底部
    }
}
