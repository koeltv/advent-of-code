package y2023

import readInput

object Day01 {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2023, "Day01_test")
    check(Day01.part1(testInput) == 0)
    check(Day01.part2(testInput) == 0)

    // apply on real input
    val input = readInput(2023, "Day01")
    println("Part 1: ${Day01.part1(input)}")
    println("Part 2: ${Day01.part2(input)}")
}
