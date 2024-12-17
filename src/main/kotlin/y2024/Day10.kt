package y2024

import readInput
import utils.Coordinates
import utils.Direction
import utils.compare
import utils.contains
import utils.get

object Day10 {
    fun parse(input: List<String>): Pair<List<List<Int>>, List<Coordinates>> {
        val map = input.map { it.map { it.digitToInt() } }

        val trailHeads = input
            .foldIndexed(listOf<Coordinates>()) { y, acc, line ->
                acc + line.withIndex()
                    .filter { (_, char) -> char == '0' }
                    .map { (x, _) -> Coordinates(x, y) }
            }

        return map to trailHeads
    }

    fun part1(input: List<String>): Int {
        val (map, trailHeads) = parse(input)

        val trailHeadsScore = trailHeads
            .associateWith { 0 }
            .toMutableMap()

        for (trailHead in trailHeads) {
            val visitedPoints = mutableSetOf<Coordinates>()
            val moveQueue = ArrayDeque(listOf(trailHead))
            do {
                val position = moveQueue.removeFirst()

                if (position in visitedPoints) continue
                visitedPoints.add(position)

                if (map[position] == 9) trailHeadsScore[trailHead] = trailHeadsScore[trailHead]!! + 1
                else {
                    Direction.values()
                        .map { direction -> position.moveToward(direction) }
                        .filter { map.contains(it) && map[it] == map[position] + 1 }
                        .forEach { moveQueue.add(it) }
                }
            } while (moveQueue.isNotEmpty())
        }

        return trailHeadsScore.values.sum()
    }

    fun part2(input: List<String>): Int {
        val (map, trailHeads) = parse(input)

        val trailHeadsScore = trailHeads
            .associateWith { 0 }
            .toMutableMap()

        for (trailHead in trailHeads) {
            val moveQueue = ArrayDeque(listOf(trailHead))
            do {
                val position = moveQueue.removeFirst()
                if (map[position] == 9) trailHeadsScore[trailHead] = trailHeadsScore[trailHead]!! + 1
                else {
                    Direction.values()
                        .map { direction -> position.moveToward(direction) }
                        .filter { map.contains(it) && map[it] == map[position] + 1 }
                        .forEach { moveQueue.add(it) }
                }
            } while (moveQueue.isNotEmpty())
        }

        return trailHeadsScore.values.sum()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day10_test")
    compare(Day10.part1(testInput), 36)
    compare(Day10.part2(testInput), 81)

    // apply on real input
    val input = readInput(2024, "Day10")
    println("Part 1: ${Day10.part1(input)}")
    println("Part 2: ${Day10.part2(input)}")
}
