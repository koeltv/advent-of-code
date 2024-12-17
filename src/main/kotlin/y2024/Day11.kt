package y2024

import readInput
import utils.compare
import utils.forBoth
import utils.splitOnNthChar

object Day11 {
    fun part1(input: List<String>): Int {
        var stoneArrangement = input[0].split(" ").map { it.toLong() }

        repeat(25) {
            stoneArrangement = stoneArrangement.flatMap { blink(it) }
        }

        return stoneArrangement.size
    }

    private fun blink(stone: Long): List<Long> {
        val newArrangement = mutableListOf<Long>()

        if (stone == 0L) newArrangement += 1
        else if (stone.toString().length % 2 == 0) {
            val stringified = stone.toString()
            stringified.splitOnNthChar(stringified.length / 2).forBoth { newArrangement += it.toLong() }
        } else {
            newArrangement += 2024L * stone
        }

        return newArrangement
    }

    fun part2(input: List<String>, blinks: Int = 75): Long {
        var stones = input[0].split(" ").associate { it.toLong() to 1L }

        repeat(blinks) {
            val stoneCount = mutableMapOf<Long, Long>()

            for ((stone, count) in stones) {
                blink(stone).forEach { stoneCount[it] = (stoneCount[it] ?: 0) + count }
            }

            stones = stoneCount
        }

        return stones.values.sum()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day11_test")
    compare(Day11.part1(testInput), 55312)
    compare(Day11.part2(testInput, blinks = 25), 55312L)

    // apply on real input
    val input = readInput(2024, "Day11")
    println("Part 1: ${Day11.part1(input)}")
    println("Part 2: ${Day11.part2(input)}")
}
