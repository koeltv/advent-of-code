package y2025

import readInput
import utils.compare

class Reactor(instructions: List<String>) {
    val nodeConnections = instructions.associate { it.substringBefore(":") to it.substringAfter(": ").split(" ") }

    private val visitedPaths = mutableMapOf<String, Long>()

    fun countPathsTo(node: String, targetNode: String, requiredNodes: List<String> = emptyList()): Long {
        if (node == targetNode) return if (requiredNodes.isEmpty()) 1 else 0
        else if (visitedPaths[node + requiredNodes] != null) return visitedPaths[node + requiredNodes]!!

        val pathLength = nodeConnections[node]!!.fold(0L) { acc, nextNode ->
            acc + countPathsTo(
                nextNode,
                targetNode,
                requiredNodes - nextNode
            )
        }
        visitedPaths[node + requiredNodes] = pathLength
        return pathLength
    }
}

object Day11 {
    fun part1(input: List<String>): Long {
        val reactor = Reactor(input)
        return reactor.countPathsTo("you", "out")
    }

    fun part2(input: List<String>): Long {
        val reactor = Reactor(input)
        return reactor.countPathsTo("svr", "out", listOf("fft", "dac"))
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day11_test")
    compare(Day11.part1(testInput), 5L)
    val testInput2 = readInput(2025, "Day11_test2")
    compare(Day11.part2(testInput2), 2L)

    // apply on real input
    val input = readInput(2025, "Day11")
    println("Part 1: ${Day11.part1(input)}")
    println("Part 2: ${Day11.part2(input)}")
}
