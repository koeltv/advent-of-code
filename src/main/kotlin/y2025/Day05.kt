package y2025

import readInput
import utils.compare
import utils.splitOn
import y2023.overlaps
import kotlin.math.max
import kotlin.math.min

object Day05 {
    fun part1(input: List<String>): Int {
        val (ranges, ids) = parse(input)

        var freshCount = 0
        for (id in ids) {
            if (ranges.any { id in it }) freshCount++
        }

        return freshCount
    }

    fun part2(input: List<String>): Long {
        val (ranges, _) = parse(input)

        return ranges.fold(mutableListOf<LongRange>()) { mergedRanges, newRange ->
            val filteredRanges = mutableListOf<LongRange>()

            var updatedRange = newRange
            while (mergedRanges.isNotEmpty()) {
                val range = mergedRanges.removeLast()
                if (updatedRange.overlaps(range)) {
                    updatedRange = min(updatedRange.first, range.first)..max(updatedRange.last, range.last)
                } else {
                    filteredRanges.add(range)
                }
            }

            filteredRanges.add(updatedRange)
            filteredRanges
        }.sumOf { it.last - it.first + 1 }
    }

    private fun parse(input: List<String>): Pair<List<LongRange>, List<Long>> =
        input.splitOn { it.isBlank() }.let { (ranges, ids) ->
            val mappedRanges = ranges.map { range ->
                val (left, right) = range.split('-')
                left.toLong()..right.toLong()
            }

            mappedRanges to ids.map { id -> id.toLong() }
        }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day05_test")
    compare(Day05.part1(testInput), 3)
    compare(Day05.part2(testInput), 14L)

    // apply on real input
    val input = readInput(2025, "Day05")
    println("Part 1: ${Day05.part1(input)}")
    println("Part 2: ${Day05.part2(input)}")
    compare(Day05.part2(input), 346240317247002L)
}
