package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day07_test")
    }

    @Test
    fun testPart1() {
        assertEquals(3749L, Day07.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(11387L, Day07.part2(testInput))
    }
}