package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day09_test")
    }

    @Test
    fun testPart1() {
        assertEquals(1928L, Day09.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(2858L, Day09.part2(testInput))
    }
}