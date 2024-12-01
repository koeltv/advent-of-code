package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day01_test")
    }

    @Test
    fun testPart1() {
        assertEquals(11, Day01.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(31, Day01.part2(testInput))
    }
}