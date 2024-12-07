package y2024

import readInput
import utils.Coordinates
import utils.Direction
import utils.compare
import utils.contains
import kotlin.streams.toList

object Day06 {
    val position = listOf('^', '<', '>', 'v')

    const val MAX_CROSS_COUNT = 7

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }
        val startingPoint = findStartingPoint(map)

        var visitedPositions = mutableSetOf<Coordinates>(startingPoint)

        var position = startingPoint
        var direction = when(map[position.y][position.x]) {
            '^' -> Direction.NORTH
            'v' -> Direction.SOUTH
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            else -> error("Unknow direction")
        }

        while (true) {
            val newPos = position.moveToward(direction)
            if (!map.contains(newPos)) {
                break
            } else if (map[newPos.y][newPos.x] == '#') {
                direction = direction.rotateRight()
            } else {
                position = newPos
                visitedPositions += position
            }
        }

        return visitedPositions.size
    }

    private fun findStartingPoint(map: List<List<Char>>): Coordinates {
        for ((y, line) in map.withIndex()) {
            for ((x, _) in line.withIndex()) {
                if (map[y][x] in position) {
                    return Coordinates(x, y)
                }
            }
        }
        return Coordinates(0, 0)
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }
        val startingPoint = findStartingPoint(map)

        val initialVisitedPositions = threadPath(startingPoint, map)

        val paradoxPositions = (initialVisitedPositions - startingPoint).keys.stream()
            .parallel()
            .filter { position ->
                val newMap = map.map { it.toMutableList() }.toMutableList()
                newMap[position.y][position.x] = '#'

                threadPath(startingPoint, newMap).values.any { it >= MAX_CROSS_COUNT }
            }.toList()

        return paradoxPositions.size
    }

    private fun threadPath(
        startingPoint: Coordinates,
        map: List<List<Char>>
    ): Map<Coordinates, Int> {
        var visitedPositions = mutableMapOf<Coordinates, Int>(startingPoint to 1)

        var position = startingPoint
        var direction = when (map[position.y][position.x]) {
            '^' -> Direction.NORTH
            'v' -> Direction.SOUTH
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            else -> error("Unknow direction")
        }

        while (visitedPositions.all { (_, count) -> count < MAX_CROSS_COUNT }) {
            val newPos = position.moveToward(direction)
            if (!map.contains(newPos)) {
                break
            } else if (map[newPos.y][newPos.x] == '#') {
                direction = direction.rotateRight()
            } else {
                position = newPos

                if (visitedPositions[position] != null) visitedPositions[position] = visitedPositions[position]!! + 1
                else visitedPositions[position] = 1
            }
        }

        return visitedPositions.toMap()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day06_test")
    compare(Day06.part1(testInput), 41)
    compare(Day06.part2(testInput), 6)

    // apply on real input
    val input = readInput(2024, "Day06")
    println("Part 1: ${Day06.part1(input)}")
    println("Part 2: ${Day06.part2(input)}")
}
