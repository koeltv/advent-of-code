package y2023

import org.junit.jupiter.api.BeforeEach
import readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private lateinit var testInput: List<String>

    @BeforeEach
    fun setup() {
        testInput = readInput(2023, "Day01_test")
    }

    @Test
    fun testPart1() {
        assertEquals(0, Day01.part1(testInput))
    }

    @Test
    fun testPart2() {
        assertEquals(0, Day01.part2(testInput))
    }
}