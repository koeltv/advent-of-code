package y2023

import readInput
import utils.*
import java.util.concurrent.atomic.AtomicInteger

fun isCorrect(arrangement: String, groups: List<Int>): Boolean {
    if (arrangement.any { it == '?' }) return false

    val arrangementGroups = arrangement
        .split(Regex("\\.+"))
        .filter { it.isNotEmpty() }

    if (arrangementGroups.size != groups.size) return false
    arrangementGroups.forEachIndexed { i, group ->
        if (i > groups.lastIndex || groups[i] != group.length) return false
    }

    return true
}

fun generatePossibilities(arrangement: String, groups: List<Int>): List<String> {
    return arrangement.foldIndexed(listOf(arrangement)) { i, acc, char ->
        if (char == '?') {
            acc.flatMap {
                listOf(
                    it.replaceRange(i, i + 1, "."),
                    it.replaceRange(i, i + 1, "#")
                )
            }.filter { isCorrect(it, groups) }
        } else acc
    }
}

private fun generatePossibilities2(arrangement: String, groups: List<Int>): List<String> {
    if (arrangement.none { it == '?' }) return listOf(arrangement)

    val maxReplacement = groups.sum()
    val possibleArrangements = arrangement.foldIndexed(listOf(arrangement)) { i, acc, char ->
        if (char == '?') {
            acc
                .filter { arr -> arr.count { it == '#' } <= maxReplacement }
                .flatMap { arr ->
                    if (arr.count { it == '#' } < maxReplacement) listOf(
                        arr.replaceRange(i, i + 1, "."),
                        arr.replaceRange(i, i + 1, "#")
                    ) else listOf(arr.replace('?', '.'))
                }
                .filter { arr -> arr.any { it == '?' } || arr.count { it == '#' } == maxReplacement }
                .filter {
                    var groupIndex = 0
                    var groupSize = 0
                    for (c in it) {
                        when (c) {
                            '?' -> return@filter true
                            '#' -> groupSize++
                            else -> {
                                if (groupSize != 0) {
                                    if (groupSize != groups[groupIndex]) return@filter false
                                    groupIndex++
                                    groupSize = 0
                                }
                            }
                        }
                        if (groupIndex < groups.size && groupSize > groups[groupIndex]) return@filter false
                    }
                    return@filter true
                }
        } else acc
    }
    return possibleArrangements
}

//fun y2023.generatePossibilities2(arrangement: String, groups: List<Int>): List<String> {
//    // Pre-parsing, obvious solutions
//    if (arrangement.none { it == '?' }) return listOf(arrangement)
//
//    // Complete the not obvious solutions
//    val maxReplacement = groups.sum()
//    val possibleArrangements = arrangement.foldIndexed(listOf(arrangement)) { i, acc, char ->
//        if (char == '?') {
//            acc
//                .filter { arr -> arr.count { it == '#' } <= maxReplacement }
//                .flatMap { arr ->
//                    if (arr.count { it == '#' } < maxReplacement) listOf(
//                        arr.replaceRange(i, i + 1, "."),
//                        arr.replaceRange(i, i + 1, "#")
//                    ) else listOf(arr.replace('?', '.'))
//                }
//                .filter { arr -> arr.any { it == '?' } || arr.count { it == '#' } == maxReplacement }
//                .filter {
//                    var groupIndex = 0
//                    var groupSize = 0
//                    for (c in it) {
//                        when (c) {
//                            '?' -> return@filter true
//                            '#' -> groupSize++
//                            else -> {
//                                if (groupSize != 0) {
//                                    if (groupSize != groups[groupIndex]) return@filter false
//                                    groupIndex++
//                                    groupSize = 0
//                                }
//                            }
//                        }
//                        if (groupIndex < groups.size && groupSize > groups[groupIndex]) return@filter false
//                    }
//                    return@filter true
//                }
//        } else acc
//    }
//    return possibleArrangements.filter { y2023.isCorrect(it, groups) }
//}

fun main() {
    fun part1(input: List<String>): Int {
        val possibilities = input.parallelStream()
            .map { line ->
                line.split(' ').let { (arrangement, groups) ->
                    arrangement to groups.split(',').map { it.toInt() }
                }
            }.map { (arrangement, groups) ->
                val unfiltered = generatePossibilities2(arrangement, groups)
                val filtered = unfiltered.filter { isCorrect(it, groups) }
                filtered
            }.toList()

        return possibilities.sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val total = input.size
        val progress = AtomicInteger(0)
        println("Starting...")

        val possibilities = input.parallelStream()
            .map { line ->
                line.split(' ').let { (arrangement, groups) ->
                    val unfoldedArrangement = "$arrangement?$arrangement?$arrangement?$arrangement?$arrangement"
                    val parsedGroup = groups.split(',').map { it.toInt() }
                    val unfoldedGroups = parsedGroup + parsedGroup + parsedGroup + parsedGroup + parsedGroup
                    unfoldedArrangement to unfoldedGroups
                }
            }.map { (arrangement, groups) ->
                val possibilities = generatePossibilities2(arrangement, groups)
                clearLine()
                colorPrint("${progress.incrementAndGet()} out of $total", EscapeSequence.Color.Green)
                possibilities
            }.toList()
        println()
        return possibilities.sumOf { it.size.toLong() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2023, "Day12_test")
    compare(part1(testInput), 21)
    compare(part2(testInput), 525152L)

    // apply on real input
    val input = readInput(2023, "Day12")
    boldPrint("\nPart 1: ${part1(input)}\n")
    boldPrint("\nPart 2: ${part2(input)}\n")
}
