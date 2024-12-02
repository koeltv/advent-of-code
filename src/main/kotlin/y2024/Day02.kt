package y2024

import readInput
import utils.compare
import utils.withoutElementAt
import kotlin.math.abs

object Day02 {
    private val safeRange = 1..3

    fun part1(input: List<String>): Int {
        return parse(input)
            .count { report -> evaluate(report) }
    }

    fun part2(input: List<String>): Int {
        return parse(input)
            .count { report ->
                if (evaluate(report)) return@count true

                for (i in report.indices) {
                    if (evaluate(report.withoutElementAt(i))) return@count true
                }

                return@count false
            }
    }

    private fun evaluate(report: List<Int>): Boolean {
        var isIncreasing: Boolean? = null
        for (i in 1..report.lastIndex) {
            val diff = report[i] - report[i - 1]
            if (abs(diff) !in safeRange) return false

            val hasIncreased = diff < 0

            if (isIncreasing == null) isIncreasing = hasIncreased
            else if (isIncreasing != hasIncreased) return false
        }
        return true
    }

    private fun parse(input: List<String>) = input
        .map { line -> line.split(" ").map { it.toInt() } }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day02_test")
    compare(Day02.part1(testInput), 2)
    compare(Day02.part2(testInput), 4)

    // apply on real input
    val input = readInput(2024, "Day02")
    println("Part 1: ${Day02.part1(input)}")
    println("Part 2: ${Day02.part2(input)}")
}