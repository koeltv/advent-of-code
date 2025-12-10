package y2025

import readInput
import utils.Coordinates
import utils.compare
import utils.toStream
import java.util.*
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Area(val p1: Coordinates, val p2: Coordinates) : Comparable<Area> {
    val sum: Long = (1 + abs(p1.x.toLong() - p2.x)) * (1 + abs(p1.y - p2.y))

    override fun compareTo(other: Area): Int {
        return this.sum.compareTo(other.sum)
    }

    override fun toString(): String {
        return "($sum)"
    }

    fun filled(): Collection<Coordinates> {
        val points = mutableSetOf<Coordinates>()
        for (x in min(p1.x, p2.x)..max(p1.x, p2.x)) {
            for (y in min(p1.y, p2.y)..max(p1.y, p2.y)) {
                points.add(Coordinates(x, y))
            }
        }
        return points
    }
}

object Day09 {
    fun part1(input: List<String>): Long {
        val initialPoints = input.map {
            val (x, y) = it.split(",")
            Coordinates(x.toInt(), y.toInt())
        }

        val areas = PriorityQueue(
            initialPoints.stream()
                .parallel()
                .flatMap { initialPoints.stream().map { p -> p to it } }
                .filter { (p1, p2) -> p1 !== p2 }
                .map { Area(it.first, it.second) }
                .collect(Collectors.toSet())
                .toSortedSet()
        )

        return areas.reversed().first().sum
    }

    fun part2(input: List<String>): Long {
        val initialPoints = input.map {
            val (x, y) = it.split(",")
            Coordinates(x.toInt(), y.toInt())
        }

        println("Computing filled loop...")
        val filledLoop = computeLoop(initialPoints)

        println("Computing possible areas...")
        val areas = initialPoints.stream()
            .parallel()
            .flatMap { initialPoints.stream().map { p -> p to it } }
            .filter { (p1, p2) -> p1 !== p2 }
            .map { Area(it.first, it.second) }
            .collect(Collectors.toSet())

        println("Filtering valid ones...")
        val validAreas = PriorityQueue(
            areas
                .stream()
                .parallel()
                .filter { area -> filledLoop.containsAll(area.filled()) }
                .collect(Collectors.toSet())
                .toSortedSet()
        )

        return validAreas.reversed().first().sum
    }

    private fun computeLoop(initialPoints: List<Coordinates>): Set<Coordinates> {
        val startingPoints = initialPoints.toMutableList()
        val borderPoints = mutableSetOf<Coordinates>()
        var p1 = startingPoints.removeFirst()
        // Do surroundings first
        while (startingPoints.isNotEmpty()) {
            val p2 = startingPoints.removeFirst()
            val xRange = if (p1.x <= p2.x) p1.x..p2.x else p2.x..p1.x
            val yRange = if (p1.y <= p2.y) p1.y..p2.y else p2.y..p1.y
            for (x in xRange) {
                for (y in yRange) {
                    borderPoints.add(Coordinates(x, y))
                }
            }
            p1 = p2
        }
        // Fill the shape
        val innerPoints = (borderPoints.minOf { it.x } + 1..borderPoints.maxOf { it.x } - 1).toStream()
            .mapToObj { it }
            .flatMap { x ->
                (borderPoints.minOf { it.y } + 1..borderPoints.maxOf { it.y } - 1).toStream().mapToObj { it }
                    .map { Coordinates(x, it) }
            }
            .parallel()
            .filter { (x, y) ->
                borderPoints.any { it.x == x && it.y < y }
                        && borderPoints.any { it.x == x && it.y > y }
                        && borderPoints.any { it.x < x && it.y == y }
                        && borderPoints.any { it.x > x && it.y == y }
            }
            .collect(Collectors.toSet())

        return borderPoints + innerPoints
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day09_test")
    compare(Day09.part1(testInput), 50L)
    compare(Day09.part2(testInput), 24L)

    // apply on real input
    val input = readInput(2025, "Day09")
    println("Part 1: ${Day09.part1(input)}")
    println("Part 2: ${Day09.part2(input)}")
}

fun Collection<Coordinates>.show() {
    for (y in 0..this.maxOf { it.y }) {
        for (x in 0..this.maxOf { it.x }) {
            if (this.contains(Coordinates(x, y))) print('#')
            else print('.')
        }
        println()
    }
}