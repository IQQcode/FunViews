package com.kunminx.linkage.section

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 分区索引的吸附判定单测，重点覆盖空分区与最后一个分区两条边界。
 */
class SectionIndexCalculatorTest {

    private val calculator = SectionIndexCalculator(listOf(3, 0, 2))

    @Test
    fun mapsPositionToSection() {
        assertEquals(5, calculator.itemCount)
        assertEquals(3, calculator.sectionCount)
        assertEquals(0, calculator.sectionOf(0))
        assertEquals(0, calculator.sectionOf(2))
        assertEquals(2, calculator.sectionOf(3))
        assertEquals(2, calculator.sectionOf(4))
        assertEquals(-1, calculator.sectionOf(5))
    }

    @Test
    fun emptySectionHasNoLastPosition() {
        assertEquals(2, calculator.lastPositionOf(0))
        assertNull(calculator.lastPositionOf(1))
        assertEquals(4, calculator.lastPositionOf(2))
    }

    @Test
    fun detectsBoundaryForSnapping() {
        assertTrue(calculator.isSectionBoundary(2))
        assertTrue(calculator.isSectionBoundary(4))
        assertFalse(calculator.isSectionBoundary(3))
        assertFalse(calculator.isSectionBoundary(1))
    }

    @Test
    fun computesIndexInsideSection() {
        assertEquals(0, calculator.indexInSection(3))
        assertEquals(1, calculator.indexInSection(4))
        assertEquals(-1, calculator.indexInSection(-1))
    }

    @Test
    fun emptyInputResolvesNothing() {
        val empty = SectionIndexCalculator(emptyList())
        assertEquals(0, empty.itemCount)
        assertEquals(-1, empty.sectionOf(0))
    }
}
