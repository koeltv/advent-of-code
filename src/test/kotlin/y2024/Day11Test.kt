package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day11_test")
    }

    @Test
    fun testPart1() {
        assertEquals(55312, Day11.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(55312L, Day11.part2(testInput))
    }
}