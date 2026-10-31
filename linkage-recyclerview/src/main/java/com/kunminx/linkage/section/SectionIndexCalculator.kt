package com.kunminx.linkage.section

/**
 * 联动列表分区索引：按每个一级分类下的二级条目数，在整体位置与
 * 「属于哪个分区、分区内第几项」之间互查。纯计算不依赖 Android，
 * 空分区（size = 0）会被自然跳过，便于单测覆盖吸附判定。
 */
class SectionIndexCalculator(sectionSizes: List<Int>) {

    private val safeSizes = sectionSizes.map { it.coerceAtLeast(0) }

    /** 每个分区第一个二级条目的整体下标。 */
    private val starts: IntArray = IntArray(safeSizes.size).also { array ->
        var acc = 0
        safeSizes.forEachIndexed { index, size ->
            array[index] = acc
            acc += size
        }
    }

    val itemCount: Int = safeSizes.sum()

    val sectionCount: Int = safeSizes.size

    /** position 越界、落在空分区或列表为空时返回 -1。 */
    fun sectionOf(position: Int): Int {
        if (position < 0 || position >= itemCount || starts.isEmpty()) return -1
        var low = 0
        var high = starts.size - 1
        while (low < high) {
            val mid = (low + high + 1) / 2
            if (starts[mid] <= position) low = mid else high = mid - 1
        }
        return low
    }

    fun firstPositionOf(section: Int): Int? = starts.getOrNull(section)

    /** 空分区没有最后一项，返回 null。 */
    fun lastPositionOf(section: Int): Int? {
        val first = firstPositionOf(section) ?: return null
        val next = starts.getOrNull(section + 1) ?: itemCount
        val last = next - 1
        return if (last < first) null else last
    }

    /** 是否是所在分区的最后一项：滑动到这里应当吸附下一个一级分类。 */
    fun isSectionBoundary(position: Int): Boolean {
        val section = sectionOf(position)
        return section >= 0 && lastPositionOf(section) == position
    }

    fun indexInSection(position: Int): Int {
        val first = firstPositionOf(sectionOf(position)) ?: return -1
        return position - first
    }
}
