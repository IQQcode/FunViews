package top.iqqcode.module.core.data

import androidx.annotation.DrawableRes

/**
 * 卡片数据
 *
 * @property title
 * @property content
 * @property portrait
 */
data class AutoDetailData(
    val title: String,
    val content: String,
    @DrawableRes
    val iconBg: Int,
    @DrawableRes
    val portrait: Int,
)
