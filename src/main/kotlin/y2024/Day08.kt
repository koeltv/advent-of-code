package y2024

import readInput
import utils.*
import kotlin.io.println

object Day08 {
    private fun parse(input: List<String>): Map<Char, List<Coordinates>> {
        val antennas = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                if (char != '.') Coordinates(x, y) to char else null
            }.filterNotNull()
        }

        val coordinatesByFrequency = antennas
            .groupBy { (_, freq) -> freq }
            .mapValues { it.value.map { it.first } }
        return coordinatesByFrequency
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }

        val coordinatesByFrequency = parse(input)

        val antinodeCoords = coordinatesByFrequency.values.map { coordinates ->
            val pairs = mutableListOf<Pair<Coordinates, Coordinates>>()
            for (i in coordinates.indices) {
                for (j in i + 1 until coordinates.size) {
                    pairs.add(coordinates[i] to coordinates[j])
                }
            }
            pairs.toList()
        }.fold(setOf<Coordinates>()) { acc, pairs ->
            val newAcc = acc.toMutableSet()
            for ((first, second) in pairs) {
                val diff = second - first
                val possibleAntinode1 = first - diff
                val possibleAntinode2 = second + diff

                if (map.contains(possibleAntinode1)) newAcc.add(possibleAntinode1)
                if (map.contains(possibleAntinode2)) newAcc.add(possibleAntinode2)
            }
            newAcc
        }

        printMap(map, antinodeCoords)

        return antinodeCoords.count()
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }

        val coordinatesByFrequency = parse(input)

        val antinodeCoords = coordinatesByFrequency.values.map { coordinates ->
            val pairs = mutableListOf<Pair<Coordinates, Coordinates>>()
            for (i in coordinates.indices) {
                for (j in i + 1 until coordinates.size) {
                    pairs.add(coordinates[i] to coordinates[j])
                }
            }
            pairs.toList()
        }.fold(setOf<Coordinates>()) { acc, pairs ->
            val newAcc = acc.toMutableSet()
            for ((first, second) in pairs) {
                val diff = second - first

                var newFirst = first
                while (map.contains(newFirst)) {
                    newAcc.add(newFirst)
                    newFirst -= diff
                }

                var newSecond = second
                while (map.contains(newSecond)) {
                    newAcc.add(newSecond)
                    newSecond += diff
                }
            }
            newAcc
        }

        printMap(map, antinodeCoords)

        return antinodeCoords.count()
    }

    private fun printMap(
        map: List<List<Char>>,
        antinodeCoords: Set<Coordinates>
    ) {
        for (y in map.indices) {
            for (x in map[0].indices) {
                val coordinates = Coordinates(x, y)
                if (antinodeCoords.contains(coordinates)) {
                    if (map[y][x] != '.') print(map[y][x].withColor(EscapeSequence.Color.RedBright))
                    else print('#'.withColor(EscapeSequence.Color.RedBright))
                } else print(map[y][x])
            }
            println()
        }
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day08_test")
    compare(Day08.part1(testInput), 14)
    compare(Day08.part2(testInput), 34)

    // apply on real input
    val input = readInput(2024, "Day08")
    println("Part 1: ${Day08.part1(input)}")
    println("Part 2: ${Day08.part2(input)}")
}
