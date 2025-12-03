package y2025

import readInput
import utils.compare

typealias Bank = List<Int>

object Day03 {
    fun part1(input: List<String>): Long {
        val banks = parse(input)
        val optimalArrangements = findOptimalArrangement(banks, 2)
        return optimalArrangements.sumOf {
            it.fold(0L) { acc, i -> acc * 10 + i }
        }
    }

    fun part2(input: List<String>): Long {
        val banks = parse(input)
        val optimalArrangements = findOptimalArrangement(banks, 12)
        return optimalArrangements.sumOf {
            it.fold(0L) { acc, i -> acc * 10 + i }
        }
    }

    private fun parse(input: List<String>): List<Bank> = input.map { it.map { c -> c.digitToInt() } }

    private fun findOptimalArrangement(banks: List<Bank>, requiredBatteries: Int): List<Bank> = banks.map { bank ->
        val values = ArrayDeque<Int>(requiredBatteries)
        var i = 0
        while (i in 0..bank.lastIndex && values.size < requiredBatteries) {
            values.add(0)
            val lastIndex = bank.lastIndex - requiredBatteries + values.size
            for (j in i..lastIndex) {
                if (bank[j] > values.last()) {
                    values.removeLast()
                    values.add(bank[j])
                    i = j
                    if (bank[j] == 9) break
                }
            }
            i++
        }
        values
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day03_test")
    compare(Day03.part1(testInput), 357L)
    compare(Day03.part2(testInput), 3121910778619L)

    // apply on real input
    val input = readInput(2025, "Day03")
    println("Part 1: ${Day03.part1(input)}")
    println("Part 2: ${Day03.part2(input)}")
}
