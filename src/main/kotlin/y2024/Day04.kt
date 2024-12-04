package y2024

import readInput
import utils.compare
import kotlin.math.abs
import kotlin.math.max

object Day04 {
    fun List<String>.getLineFrom(from: Pair<Int, Int>, to: Pair<Int, Int>): String {
        val (y1, x1) = from
        val (y2, x2) = to

        val diffX = x2 - x1
        val dx = if (diffX < 0) -1 else if (diffX > 0) 1 else 0
        val diffY = y2 - y1
        val dy = if (diffY < 0) -1 else if (diffY > 0) 1 else 0

        require(x1 == x2 || y1 == y2 || abs(diffY) == abs(diffX))

        val buffer = StringBuffer()
        for (i in 0..max(abs(diffX), abs(diffY))) {
            buffer.append(this[y1 + i * dy][x1 + i * dx])
        }

        return buffer.toString()
    }

    fun String.containsEitherWay(content: String) = this.contains(content) || this.reversed().contains(content)

    fun part1(input: List<String>): Int {
        var count = 0
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (input[y][x] == 'X') {
                    val roomLeft = x >= 3
                    val roomRight = x <= input[0].lastIndex - 3
                    val roomAbove = y >= 3
                    val roomBelow = y <= input.lastIndex - 3

                    if (roomAbove && input.getLineFrom(y to x, y - 3 to x) == "XMAS") count++
                    if (roomBelow && input.getLineFrom(y to x, y + 3 to x) == "XMAS") count++
                    if (roomLeft && input.getLineFrom(y to x, y to x - 3) == "XMAS") count++
                    if (roomRight && input.getLineFrom(y to x, y to x + 3) == "XMAS") count++

                    if (roomAbove && roomLeft && input.getLineFrom(y to x, y - 3 to x - 3) == "XMAS") count++
                    if (roomAbove && roomRight && input.getLineFrom(y to x, y - 3 to x + 3) == "XMAS") count++
                    if (roomBelow && roomLeft && input.getLineFrom(y to x, y + 3 to x - 3) == "XMAS") count++
                    if (roomBelow && roomRight && input.getLineFrom(y to x, y + 3 to x + 3) == "XMAS") count++
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (input[y][x] == 'A') {
                    val roomLeft = x >= 1
                    val roomRight = x <= input[0].lastIndex - 1
                    val roomAbove = y >= 1
                    val roomBelow = y <= input.lastIndex - 1

                    if (roomAbove && roomBelow && roomLeft && roomRight) {
                        if (
                            input.getLineFrom(y - 1 to x - 1, y + 1 to x + 1).containsEitherWay("MAS") &&
                            input.getLineFrom(y - 1 to x + 1, y + 1 to x - 1).containsEitherWay("MAS")
                        ) count++
                    }
                }
            }
        }
        return count
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day04_test")
    compare(Day04.part1(testInput), 18)
    compare(Day04.part2(testInput), 9)

    // apply on real input
    val input = readInput(2024, "Day04")
    println("Part 1: ${Day04.part1(input)}")
    println("Part 2: ${Day04.part2(input)}")
}
