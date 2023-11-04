package top.iqqcode.module.core.weight.list

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import top.iqqcode.module.core.R


/**
 * Content View holder
 *
 * @param itemView
 * @constructor
 */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cardView: CardView = itemView.findViewById(R.id.cardView)
    var cardContainer: LinearLayout = itemView.findViewById(R.id.cardContainer)
    var elementIcon: ImageView = itemView.findViewById(R.id.elementIcon)
    var titleView: TextView = itemView.findViewById(R.id.title)
    var contentView: TextView = itemView.findViewById(R.id.content)
}

/**
 * Header view holder
 *
 * @param itemView
 * @constructor
 */
class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var headerTitle: TextView = itemView.findViewById(R.id.headerTitle)
    var subTitle: TextView = itemView.findViewById(R.id.subHeaderTitle)
}

/**
 * Footer view holder
 *
 * @param itemView
 * @constructor
 */
class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var headerTitle: TextView = itemView.findViewById(R.id.footerTitle)
    var subTitle: TextView = itemView.findViewById(R.id.subFooterContent)
}

