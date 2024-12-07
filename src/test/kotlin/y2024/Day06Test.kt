package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day06_test")
    }

    @Test
    fun testPart1() {
        assertEquals(41, Day06.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(6, Day06.part2(testInput))
    }
}