package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day10_test")
    }

    @Test
    fun testPart1() {
        assertEquals(36, Day10.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(81, Day10.part2(testInput))
    }
}