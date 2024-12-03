package y2024

import readInput
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {
    private lateinit var testInput: List<String>
    private lateinit var testInput2: List<String>

    @BeforeTest
    fun setup() {
        testInput = readInput(2024, "Day03_test")
        testInput2 = readInput(2024, "Day03_test2")
    }

    @Test
    fun testPart1() {
        assertEquals(161, Day03.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(48, Day03.part2(testInput2))
    }
}