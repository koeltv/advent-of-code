package y2024

import readInput
import utils.compare
import kotlin.math.abs

object Day01 {
    fun part1(input: List<String>): Int {
        return parse(input)
            .let { (left, right) ->
                left.sorted() zip right.sorted()
            }.sumOf { (left, right) ->
                abs(left - right)
            }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parse(input)

        val similarities = right.groupingBy { it }.eachCount()

        return left.sumOf { it * (similarities[it] ?: 0) }
    }

    private fun parse(input: List<String>) = input
        .map {
            val (left, right) = Regex("(\\d+) +(\\d+)").find(it)!!.destructured
            left.toInt() to right.toInt()
        }.unzip()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day01_test")
    compare(Day01.part1(testInput), 11)
    compare(Day01.part2(testInput), 31)

    // apply on real input
    val input = readInput(2024, "Day01")
    println("Part 1: ${Day01.part1(input)}")
    println("Part 2: ${Day01.part2(input)}")
}
