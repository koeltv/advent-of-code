package y2025

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    private lateinit var testInput: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2025, "Day12_test")
    }

    @Test
    fun testPart1() {
        assertEquals(0, Day12.part1(testInput))
    }
}