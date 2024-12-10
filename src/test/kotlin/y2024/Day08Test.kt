package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day08_test")
    }

    @Test
    fun testPart1() {
        assertEquals(14, Day08.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(34, Day08.part2(testInput))
    }
}