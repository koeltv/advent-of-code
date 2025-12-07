package y2025

import readInput
import utils.Coordinates
import utils.Direction
import utils.compare
import utils.contains

object Day07 {
    fun part1(map: List<String>): Int {
        val startingPoint = Coordinates(map[0].indexOf('S'), 0)
        val exploredPositions = mutableSetOf<Coordinates>()
        exploredPositions.add(startingPoint)
        val propagatingBeams = ArrayDeque<Coordinates>()
        propagatingBeams.add(startingPoint)

        var splitCount = 0
        while (propagatingBeams.isNotEmpty()) {
            val currentBeam = propagatingBeams.removeFirst().moveToward(Direction.SOUTH)
            if (currentBeam in exploredPositions || !map.contains(currentBeam)) continue

            if (map[currentBeam.y][currentBeam.x] != '^') {
                exploredPositions.add(currentBeam)
                propagatingBeams.add(currentBeam)
            } else {
                splitCount++
                for (direction in listOf(Direction.WEST, Direction.EAST)) {
                    val sideBeam = currentBeam.moveToward(direction)
                    if (map.contains(sideBeam) && sideBeam !in exploredPositions) {
                        exploredPositions.add(sideBeam)
                        propagatingBeams.add(sideBeam)
                    }
                }
            }
        }

        return splitCount
    }

    fun part2(map: List<String>): Long {
        val startingBeam = Coordinates(map[0].indexOf('S'), 0)
        return computeRealityCount(map, startingBeam)
    }

    private fun computeRealityCount(
        map: List<String>,
        startingBeam: Coordinates,
        exploredRealitiesCount: MutableMap<Coordinates, Long> = mutableMapOf()
    ): Long {
        val updatedBeam = startingBeam.moveToward(Direction.SOUTH)

        if (updatedBeam in exploredRealitiesCount.keys) {
            return exploredRealitiesCount[updatedBeam]!!
        } else if (!map.contains(updatedBeam)) {
            exploredRealitiesCount[updatedBeam] = 1
            return 1
        } else if (map[updatedBeam.y][updatedBeam.x] != '^') {
            return computeRealityCount(map, updatedBeam, exploredRealitiesCount)
        }

        var realitiesCount = 0L
        for (direction in listOf(Direction.WEST, Direction.EAST)) {
            val sideBeam = updatedBeam.moveToward(direction)
            if (sideBeam in exploredRealitiesCount.keys) {
                realitiesCount += exploredRealitiesCount[sideBeam]!!
            } else if (map.contains(sideBeam)) {
                val sideRealitiesCount = computeRealityCount(map, sideBeam, exploredRealitiesCount)
                realitiesCount += sideRealitiesCount
                exploredRealitiesCount[sideBeam] = sideRealitiesCount
            }
        }
        return realitiesCount
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day07_test")
    compare(Day07.part1(testInput), 21)
    compare(Day07.part2(testInput), 40L)

    // apply on real input
    val input = readInput(2025, "Day07")
    println("Part 1: ${Day07.part1(input)}")
    println("Part 2: ${Day07.part2(input)}")
}
