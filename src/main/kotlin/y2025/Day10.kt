package y2025

import readInput
import utils.compare
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

typealias Button = List<Int>
typealias State = List<Boolean>
typealias Counters = List<Int>

data class Machine(val targetState: State, val buttons: List<Button>, val joltages: Counters) {
    companion object {
        val MACHINE_REGEX = Regex("\\[([.#]+)] (.+) \\{(.+)}")

        fun parse(input: String): Machine {
            val (targetState, buttons, joltages) = MACHINE_REGEX.matchEntire(input)!!.destructured
            return Machine(
                targetState.map { it != '.' },
                buttons.split(' ').map { buttonString ->
                    buttonString.removeSurrounding("(", ")").split(",").map { it.toInt() }
                },
                joltages.split(",").map { it.toInt() }
            )
        }
    }

    // PART 1

    fun targetStateReached(state: State): Boolean {
        for ((i, light) in state.withIndex()) {
            if (targetState[i] != light) return false
        }
        return true
    }

    fun Button.press(previousState: State): State {
        val newState = previousState.toMutableList()
        for (i in this) {
            newState[i] = !newState[i]
        }
        return newState
    }

    fun minimumButtonPresses(): Int {
        val initialState = ArrayList<Boolean>(targetState.size)
        targetState.indices.forEach { _ -> initialState.add(false) }

        val sortedStates = PriorityQueue<Pair<State, Int>>(compareBy { (_, steps) -> steps })
        sortedStates.add(Pair(initialState, 0))

        while (true) {
            val (state, presses) = sortedStates.poll()
            for (button in buttons) {
                val updatedState = button.press(state)

                if (targetStateReached(updatedState)) return presses + 1

                sortedStates.add(Pair(updatedState, presses + 1))
            }
        }
    }

    // PART 2

    fun Button.increaseCounters(counters: Counters): Counters {
        val updatedCounters = counters.toMutableList()
        for (i in this) {
            updatedCounters[i] += 1
        }
        return updatedCounters
    }

    fun joltagesReached(counters: Counters): Boolean {
        for ((i, count) in counters.withIndex()) {
            if (count != joltages[i]) return false
        }
        return true
    }

    fun computeDistanceFromTargetJoltages(counts: Counters): Long {
        var distance = 0L
        for ((i, count) in counts.withIndex()) {
            distance += joltages[i] - count
        }
        return distance
    }

    private fun joltageInThreshold(counters: Counters): Boolean {
        for ((i, count) in counters.withIndex()) {
            if (count > joltages[i]) return false
        }
        return true
    }

    fun minimumButtonPressesForJoltage(): Long {
        val initialCounts = ArrayList<Int>(targetState.size)
        targetState.indices.forEach { _ -> initialCounts.add(0) }

        val sortedJoltages =
            PriorityQueue(compareBy<Triple<Counters, Long, Long>> { (_, _, distance) -> distance }.thenComparing { (_, steps, _) -> steps })
        sortedJoltages.add(Triple(initialCounts, 0L, computeDistanceFromTargetJoltages(initialCounts)))

        while (true) {
            val (counters, steps, _) = sortedJoltages.poll()
            for (button in buttons) {
                val updatedCounters = button.increaseCounters(counters)

                if (joltagesReached(updatedCounters)) return steps + 1
                else if (joltageInThreshold(updatedCounters)) {
                    sortedJoltages.add(
                        Triple(
                            updatedCounters,
                            steps + 1,
                            computeDistanceFromTargetJoltages(updatedCounters)
                        )
                    )
                }
            }
        }
    }
}

object Day10 {
    fun part1(input: List<String>): Int {
        val machines = input.map { Machine.parse(it) }
        return machines.sumOf { it.minimumButtonPresses() }
    }

    fun part2(input: List<String>): Long {
        val machines = input.map { Machine.parse(it) }

        val progressCounter = AtomicInteger(0)

        return machines.stream()
            .parallel()
            .mapToLong {
                val presses = it.minimumButtonPressesForJoltage()
                println("Progress: ${progressCounter.addAndGet(1)}/${machines.size}")
                presses
            }
            .sum()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2025, "Day10_test")
    compare(Day10.part1(testInput), 7)
    compare(Day10.part2(testInput), 33L)

    // apply on real input
    val input = readInput(2025, "Day10")
    println("Part 1: ${Day10.part1(input)}")
    println("Part 2: ${Day10.part2(input)}")
}
