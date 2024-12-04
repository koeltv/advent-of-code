package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day04_test")
    }

    @Test
    fun testPart1() {
        assertEquals(18, Day04.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(9, Day04.part2(testInput))
    }
}