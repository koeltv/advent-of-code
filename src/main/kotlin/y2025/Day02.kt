package y2025

import readInput
import utils.compare
import utils.toStream
import java.util.concurrent.ConcurrentHashMap

object Day02 {
    val UNIQUE_REPETITION_REGEX = Regex("^(.+)\\1$")
    val MULTIPLE_REPETITION_REGEX = Regex("^(.+)(\\1)+$")

    fun part1(input: List<String>): Long {
        val ranges = parse(input)

        val resultMap = ConcurrentHashMap<Long, Boolean>()

        val sumOf = ranges
            .stream()
            .flatMapToLong { it.toStream() }
            .parallel()
            .filter { id ->
                resultMap.computeIfAbsent(id) {
                    hasRepetitionPattern(it)
                }
            }
            .sum()

        return sumOf
    }

    fun part2(input: List<String>): Long {
        val ranges = parse(input)

        val resultMap = ConcurrentHashMap<Long, Boolean>()

        val sumOf = ranges
            .stream()
            .flatMapToLong { it.toStream() }
            .parallel()
            .filter { id ->
                resultMap.computeIfAbsent(id) {
                    hasMultipleRepetitionPattern(it)
                }
            }
            .sum()

        return sumOf
    }

    // Too slow
    @Suppress("unused")
    private fun hasRepetitionPatternV1(id: Long): Boolean {
        val string = "$id"
        var partial = string[0].toString()
        var i = 0
        do {
            if (string.length % (i + 1) != 0) continue

            val parts = string.windowed(i + 1, step = i + 1)
            if (parts.all { part -> part == parts[0] }) return true

            partial += string[i++]
        } while (i + 1 <= string.length / 2)
        return false
    }

    private fun hasRepetitionPattern(id: Long): Boolean {
        return UNIQUE_REPETITION_REGEX.matches(id.toString())
    }

    private fun hasMultipleRepetitionPattern(id: Long): Boolean {
        return MULTIPLE_REPETITION_REGEX.matches(id.toString())
    }

    private fun parse(input: List<String>): List<LongRange> {
        val ranges = input[0].split(',').map {
            val (start, end) = it.split('-')
            start.toLong()..end.toLong()
        }
        return ranges
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day02_test")
    compare(Day02.part1(testInput), 1227775554L)
    compare(Day02.part2(testInput), 4174379265L)

    // apply on real input
    val input = readInput(2025, "Day02")
    println("Part 1: ${Day02.part1(input)}")
    println("Part 2: ${Day02.part2(input)}")
}
