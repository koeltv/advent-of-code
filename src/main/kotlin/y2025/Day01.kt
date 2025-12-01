package y2025

import readInput
import utils.compare

object Day01 {
    const val MAX_VALUE = 99
    const val STARTING_VALUE = 50

    fun part1(input: List<String>): Int {
        val instructions = parse(input).map { it.first to (it.second % (MAX_VALUE + 1)) }

        var zeroCount = 0
        var current = STARTING_VALUE
        for ((action, count) in instructions) {
            if (action == 'L') current -= count
            else if (action == 'R') current += count

            if (current < 0) current += MAX_VALUE + 1
            else if (current > MAX_VALUE) current -= MAX_VALUE + 1

            if (current == 0) zeroCount++
        }

        return zeroCount
    }

    fun part2(input: List<String>): Int {
        val instructions = parse(input)

        var zeroCount = 0
        var previousValue = STARTING_VALUE
        for ((action, count) in instructions) {
            val newRawValue =
                if (action == 'L') previousValue - count % (MAX_VALUE + 1)
                else previousValue + count % (MAX_VALUE + 1)

            if (previousValue != 0 && newRawValue !in 1..MAX_VALUE) zeroCount++

            previousValue =
                if (newRawValue < 0) newRawValue + MAX_VALUE + 1
                else if (newRawValue > MAX_VALUE) newRawValue - (MAX_VALUE + 1)
                else newRawValue

            val wholeTurns = count / (MAX_VALUE + 1)
            zeroCount += wholeTurns
        }

        return zeroCount
    }

    private fun parse(input: List<String>): List<Pair<Char, Int>> {
        val instructions = input.map {
            val (action, count) = Regex("([LR])(\\d+)").find(it)!!.destructured
            action[0] to count.toInt()
        }
        return instructions
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day01_test")
    compare(Day01.part1(testInput), 3)
    compare(Day01.part2(testInput), 6)

    // apply on real input
    val input = readInput(2025, "Day01")
    println("Part 1: ${Day01.part1(input)}")
    println("Part 2: ${Day01.part2(input)}")
}
