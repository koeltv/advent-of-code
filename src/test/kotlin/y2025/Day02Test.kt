package y2025

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2025, "Day02_test")
    }

    @Test
    fun testPart1() {
        assertEquals(0, Day02.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(0, Day02.part2(testInput))
    }
}