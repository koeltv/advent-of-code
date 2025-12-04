package y2025

import readInput
import utils.compare
import kotlin.math.max
import kotlin.math.min

object Day04 {
    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }

        val moveablePaperCount = map.indices.sumOf { y ->
            map[y].indices.count { x ->
                map[x][y] == '@' && map.isMoveable(x, y)
            }
        }

        return moveablePaperCount
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray() }

        var moveablePaperCount = 0
        val moveablePapersCoordinates = mutableListOf<Pair<Int, Int>>()
        do {
            // Update Map
            for ((y, x) in moveablePapersCoordinates) map[y][x] = '.'
            moveablePapersCoordinates.clear()
            // Search for moveable paper rolls
            for (y in map.indices) {
                for (x in map[y].indices) {
                    if (map[y][x] == '@' && map.isMoveable(y, x)) {
                        moveablePapersCoordinates.add(y to x)
                        moveablePaperCount++
                    }
                }
            }
        } while (moveablePapersCoordinates.isNotEmpty())

        return moveablePaperCount
    }

    private fun List<CharArray>.isMoveable(y: Int, x: Int): Boolean {
        var surroundingPapers = 0

        for (i in max(0, y - 1)..min(lastIndex, y + 1)) {
            for (j in max(0, x - 1)..min(this[y].lastIndex, x + 1)) {
                if (i == y && j == x) continue
                if (this[i][j] == '@') surroundingPapers++
            }
        }

        return surroundingPapers < 4
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day04_test")
    compare(Day04.part1(testInput), 13)
    compare(Day04.part2(testInput), 43)

    // apply on real input
    val input = readInput(2025, "Day04")
    println("Part 1: ${Day04.part1(input)}")
    println("Part 2: ${Day04.part2(input)}")
}
