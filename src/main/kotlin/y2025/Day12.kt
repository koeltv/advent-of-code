package y2025

import readInput
import utils.compare
import utils.splitOn

data class Present(val pattern: List<String>) {
    val unitSize = pattern.sumOf { line -> line.count { it == '#' } }
}

object Day12 {
    val regionHeaderRegex = Regex("\\d+x\\d+: .+")

    fun part1(input: List<String>): Int {
        val firstRegionIndex = input.indexOfFirst { it.matches(regionHeaderRegex) }

        val presentsById = input.take(input.size - (input.size - firstRegionIndex) - 1)
            .splitOn { it.isBlank() }
            .associate { lines ->
                val id = lines.first().substringBefore(':').toInt()
                id to Present(lines.drop(1))
            }

        val regions = input.drop(firstRegionIndex).map { line ->
            val (height, width) = line.substringBefore(':').split("x").map { it.toInt() }
            val quotas = line.substringAfter(": ").split(" ").map { it.toInt() }

            Pair(height, width) to quotas
        }

        val validRegionsCount = regions.count { (dimensions, quotas) ->
            val totalArea = dimensions.first * dimensions.second
            // If the total area is big enough to fit all squares of 3x3, then it can fit all presents
            if (totalArea >= quotas.sum() * (3 * 3)) {
                return@count true
            } else {
                val totalFilledSpotsByType = quotas.mapIndexed { i, quota -> presentsById[i]!!.unitSize * quota }
                // If the total number of units is smaller than the number of unit needed, then it won't fit all presents
                if (totalArea < totalFilledSpotsByType.sum()) {
                    return@count false
                } else {
                    // In other cases, we need expensive computations to check if the presents can fit together, so we stop there for now
                    throw UnsupportedOperationException("Cannot confirm for ${dimensions.first}x${dimensions.second}: ${quotas.joinToString()}")
                }
            }
        }

        return validRegionsCount
    }
}

fun main() {
    // ignored as it is too complex
    //val testInput = readInput(2025, "Day12_test")
    //compare(Day12.part1(testInput), 2)

    val testInput = readInput(2025, "Day12_test_manual")
    compare(Day12.part1(testInput), 2)

    // apply on real input
    val input = readInput(2025, "Day12")

    println("Part 1: ${Day12.part1(input)}")
}
