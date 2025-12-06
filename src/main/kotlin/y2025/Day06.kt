package y2025

import readInput
import utils.compare

data class MathProblem(val numbers: List<Long>, val operation: Char) {
    fun solve(): Long {
        return when (operation) {
            '+' -> numbers.sum()
            '*' -> numbers.fold(1L) { acc, element -> acc * element }
            else -> throw IllegalArgumentException()
        }
    }
}

object Day06 {
    fun part1(input: List<String>): Long {
        val problems = parsePart1(input)
        return problems.sumOf { it.solve() }
    }

    fun part2(input: List<String>): Long {
        val problems = parsePart2(input)
        return problems.sumOf { it.solve() }
    }

    private fun parsePart1(input: List<String>): List<MathProblem> {
        val sheet = input
            .dropLast(1)
            .map { it.removeSurrounding(" ") }
            .map { line -> line.split(" ").filter { it.isNotBlank() }.map { it.toLong() } }

        val operation = input.last().split(" ")
            .filter { it.isNotBlank() }
            .map { it[0] }

        val problems = mutableListOf<MathProblem>()
        for (x in sheet[0].indices) {
            val values = mutableListOf<Long>()
            for (y in sheet.indices) {
                values += sheet[y][x]
            }
            problems += MathProblem(values, operation[x])
        }
        return problems
    }

    private fun parsePart2(input: List<String>): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        var x = input[0].lastIndex

        if (input.any { it.lastIndex != x }) throw IllegalArgumentException("The lines have different lengths")

        val numbers = mutableListOf<Long>()
        while (x >= 0) {
            var endFound = false
            var acc = 0L
            for (y in input.indices) {
                if (input[y][x] == ' ') continue

                if (input[y][x] == '+' || input[y][x] == '*') {
                    numbers.add(acc)
                    problems.add(MathProblem(numbers.toList(), input[y][x]))
                    endFound = true
                    x--
                    break
                }

                acc = acc * 10 + input[y][x].digitToInt()
            }
            if (!endFound) numbers.add(acc)
            else numbers.clear()
            x--
        }


        return problems
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day06_test")
    compare(Day06.part1(testInput), 4277556L)
    compare(Day06.part2(testInput), 3263827L)

    // apply on real input
    val input = readInput(2025, "Day06")
    println("Part 1: ${Day06.part1(input)}")
    println("Part 2: ${Day06.part2(input)}")
}
