package y2025

import readInput
import utils.compare
import java.util.*
import java.util.stream.Collectors
import kotlin.math.sqrt

data class Coordinates3D(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(point: Coordinates3D): Double {
        val dx = point.x - this.x
        val dy = point.y - this.y
        val dz = point.z - this.z
        return sqrt(dx.toDouble() * dx + dy * dy + dz * dz)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }
}

abstract class Circuit(val points: List<Coordinates3D>) {
    fun connectsTo(circuit: Circuit): Boolean {
        return this.points.any { circuit.points.contains(it) }
    }

    fun merge(circuit: Circuit): Circuit {
        val points = (this.points + circuit.points).toSet()
        return CompositeCircuit(points.toList())
    }

    fun contains(point: Coordinates3D): Boolean {
        return point in points
    }

    fun size(): Int = points.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        else if (other is Circuit) {
            return this.points.size == other.points.size && this.points.containsAll(other.points)
        }
        return false
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }

    override fun toString(): String {
        return points.joinToString(separator = ",", prefix = "[", postfix = "]")
    }
}

class SimpleCircuit(p1: Coordinates3D, p2: Coordinates3D) : Circuit(listOf(p1, p2)) {
    fun internalDistance(): Double {
        return points[0].distanceTo(points[1])
    }
}

class JunctionBox(point: Coordinates3D) : Circuit(listOf(point))

class CompositeCircuit(points: List<Coordinates3D>) : Circuit(points)

object Day08 {
    fun part1(input: List<String>, connections: Int, debug: Boolean = false): Int {
        val initialPoints = input.map {
            val (x, y, z) = it.split(",")
            Coordinates3D(x.toInt(), y.toInt(), z.toInt())
        }

        val possibleCircuits = PriorityQueue(
            initialPoints.stream()
                .parallel()
                .flatMap { initialPoints.stream().map { p -> p to it } }
                .filter { (p1, p2) -> p1 !== p2 }
                .map { SimpleCircuit(it.first, it.second) }
                .collect(Collectors.toSet())
                .toSortedSet { c1, c2 -> c1.internalDistance().compareTo(c2.internalDistance()) }
        )

        val connectedCircuits = mutableListOf<Circuit>()
        repeat(connections) { count ->
            val firstCircuit = possibleCircuits.poll()

            if (connectedCircuits.isEmpty()) {
                connectedCircuits.add(firstCircuit)
            } else {
                var currentCircuit: Circuit = firstCircuit
                var processCount = connectedCircuits.size
                while (processCount > 0) {
                    val circuit = connectedCircuits.removeFirst()
                    if (currentCircuit.connectsTo(circuit)) {
                        currentCircuit = currentCircuit.merge(circuit)
                    } else {
                        connectedCircuits.add(circuit)
                    }
                    processCount--
                }
                connectedCircuits.add(currentCircuit)
            }

            if (debug) {
                println("Iteration n°${count + 1}")
                connectedCircuits.sortedBy { it.size() }.reversed().groupBy { it.size() }.forEach { (size, list) ->
                    print("${list.size} circuits of size $size, ")
                }
                print("${initialPoints.size - connectedCircuits.sumOf { it.points.size }} circuits of size 1")
                println()
            }
        }
        initialPoints
            .filter { connectedCircuits.none { circuit -> circuit.contains(it) } }
            .forEach { point -> connectedCircuits.add(JunctionBox(point)) }

        val (c1, c2, c3) = connectedCircuits.sortedBy { it.size() }.reversed()

        return c1.size() * c2.size() * c3.size()
    }

    fun part2(input: List<String>): Int {
        return 0
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day08_test")
    compare(Day08.part1(testInput, 10, debug = true), 40)
    compare(Day08.part2(testInput), 0)

    // apply on real input
    val input = readInput(2025, "Day08")
    println("Part 1: ${Day08.part1(input, 1000)}") // > 12560
    println("Part 2: ${Day08.part2(input)}")
}
