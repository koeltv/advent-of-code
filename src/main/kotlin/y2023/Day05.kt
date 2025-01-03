package y2023

import readInput
import utils.printline
import utils.splitOn
import utils.toStream
import kotlin.streams.asStream

data class LookupTable(val source: String, val destination: String, val table: Map<LongRange, LongRange>) {
    fun convert(value: Long): Long {
        return table.entries
            .find { (startRange, _) -> value in startRange }
            ?.let { (startRange, targetRange) -> targetRange.first + (value - startRange.first) }
            ?: value
    }

    companion object {
        fun parseFrom(input: List<String>): LookupTable {
            val (source, target) = Regex("(\\w+)-to-(\\w+) map:").find(input[0])!!.destructured
            val table = input.drop(1).associate { line ->
                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.split(" ").map { it.toLong() }
                (sourceRangeStart until sourceRangeStart + rangeLength) to (destinationRangeStart until destinationRangeStart + rangeLength)
            }

            return LookupTable(source, target, table)
        }
    }
}

data class Almanac(private val tables: List<LookupTable>) {
    private val cache = mutableMapOf<Pair<String, String>, List<LookupTable>>()

    fun convert(source: String, target: String, value: Long): Long {
        val conversionChain = cache[source to target] ?: buildConversionChain(source, target)

        return conversionChain.fold(value) { currentValue, lookupTable ->
            lookupTable.convert(currentValue)
        }
    }

    private fun buildConversionChain(source: String, target: String): List<LookupTable> {
        val chain = mutableListOf<LookupTable>()
        var currentSource = source
        do {
            val table = tables.first { it.source == currentSource }
            chain.add(table)
            currentSource = table.destination
            cache[source to currentSource] = chain.toList()
        } while (chain.last().destination != target)
        return chain
    }

    companion object {
        fun parseFrom(input: List<String>): Almanac {
            return Almanac(
                input
                    .splitOn { it.isBlank() }
                    .map { LookupTable.parseFrom(it) }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val almanac = Almanac.parseFrom(input.drop(2))

        return input[0].removePrefix("seeds: ")
            .split(" ")
            .map { it.toLong() }
            .minOf { almanac.convert("seed", "location", it) }
    }

    fun part2(input: List<String>): Long {
        val almanac = Almanac.parseFrom(input.drop(2))

        val minSeedNumber = Regex("(\\d+) (\\d+)").findAll(input[0]).asStream().parallel()
            .map {
                it.destructured.let { (start, length) ->
                    val firstSeedNumber = start.toLong()
                    val seedRangeLength = length.toLong()
                    firstSeedNumber until firstSeedNumber + seedRangeLength
                }
            }.flatMapToLong { it.toStream() }
            .map { almanac.convert("seed", "location", it) }
            .min().asLong

        return minSeedNumber
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2023, "Day05_test")
    check(part1(testInput) == 35L)

    check(part2(testInput) == 46L)

    // apply on real input
    val input = readInput(2023, "Day05")
    part1(input).printline()
    part2(input).printline()
}
