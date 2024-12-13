package y2024

import readInput
import utils.compare
import utils.size

object Day09 {
    fun part1(input: List<String>): Long {
        val layout = input[0]
        val diskMap = layout.expand()
        println("DiskMap: $diskMap")
        val compacted = diskMap.compact()
        println("Compacted: $compacted")
        return compacted.checksum()
    }

    fun part2(input: List<String>): Long {
        val layout = input[0]
        val diskMap = layout.expand()
        println("DiskMap: $diskMap")
        val compacted = diskMap.compactWholeBlocks()
        println("Compacted: $compacted")
        return compacted.checksum()
    }

    fun String.expand(): List<String> {
        val builder = mutableListOf<String>()
        for ((i, c) in this.withIndex()) {
            val blockSize = c.digitToInt()
            val newChar = if (i % 2 == 0) "${i/2}" else "."
            repeat(blockSize) { builder.add(newChar) }
        }
        return builder.toList()
    }

    fun List<String>.compact(): List<String> {
        var temp = this.toMutableList()

        var leftMostFreeIndex = temp.indexOfFirst { it == "." }
        var lastOccupiedIndex = temp.indexOfLast { it != "." }

        while (leftMostFreeIndex < lastOccupiedIndex) {
            temp[leftMostFreeIndex] = temp[lastOccupiedIndex]
            temp[lastOccupiedIndex] = "."

            leftMostFreeIndex = temp.indexOfFirst { it == "." }
            lastOccupiedIndex = temp.indexOfLast { it != "." }
        }

        return temp.toList()
    }

    fun List<String>.compactWholeBlocks(): List<String> {
        var copy = this.toMutableList()

        val blockRanges = mutableListOf<Pair<String, IntRange>>()
        var previous = this[0]
        var startIndex = 0
        for ((i, part) in this.withIndex()) {
            if (previous != part) {
                blockRanges.add(previous to startIndex..i-1)
                startIndex = i
                previous = part
            }
        }
        blockRanges.add(previous to startIndex..this.lastIndex)

        for ((i, value) in blockRanges.withIndex().reversed()) {
            val (fileId, range) = value
            if (fileId == ".") continue

            for (j in 0 until i) {
                val (inspectedFileId, inspectedRange) = blockRanges[j]
                if (inspectedFileId == "." && inspectedRange.size() >= range.size()) {
                    blockRanges[j] = "." to (inspectedRange.start+range.size())..inspectedRange.last
                    for (k in 0 until range.size()) {
                        copy[inspectedRange.start+k] = fileId
                        copy[range.start+k] = "."
                    }
                    break
                }
            }
        }

        return copy.toList()
    }

    fun List<String>.checksum(): Long {
        return this
            .mapIndexed { i, number -> if (number != ".") i * number.toLong() else 0 }
            .sum()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day09_test")
    compare(Day09.part1(testInput), 1928L)
    compare(Day09.part2(testInput), 2858L)

    // apply on real input
    val input = readInput(2024, "Day09")
    println("Part 1: ${Day09.part1(input)}")
    println("Part 2: ${Day09.part2(input)}")
}
