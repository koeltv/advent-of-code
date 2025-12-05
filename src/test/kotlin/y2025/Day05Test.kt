package y2025

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2025, "Day05_test")
    }

    @Test
    fun testPart1() {
        assertEquals(0, Day05.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(0, Day05.part2(testInput))
    }
}