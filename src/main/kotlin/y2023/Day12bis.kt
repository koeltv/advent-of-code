package y2023

import readInput
import utils.boldPrint
import utils.compare

data class CounterState(val springIndex: Int = 0, val groupIndex: Int = 0, val damageCount: Int = 0) {
    fun next(spring: Boolean = false, group: Boolean = false, damage: Boolean = false) = CounterState(
        springIndex = springIndex + (if (spring) 1 else 0),
        groupIndex = groupIndex + (if (group) 1 else 0),
        damageCount = if (group) 0 else damageCount + (if (damage) 1 else 0),
    )
}

fun countArrangements(row: String, foldFactor: Int = 1): Long {
    val (springsStr, groupsStr) = row.split(' ')
    val groupList = groupsStr.split(',').map { it.toInt() }

    val springs = (1..foldFactor).joinToString("?") { springsStr }
    val groups = (1..foldFactor).flatMap { groupList }
    val cacheForState = mutableMapOf<CounterState, Long>()

    fun count(state: CounterState): Long {
        return if (state.springIndex >= springs.length) {
            if (
                state.groupIndex >= groups.size ||
                state.groupIndex == groups.lastIndex && state.damageCount == groups[state.groupIndex]
            ) 1
            else 0
        } else {
            when (state) {
                in cacheForState -> cacheForState.getValue(state)
                else -> {
                    var arr = 0L
                    when (springs[state.springIndex]) {
                        '?', '.' -> {
                            if (state.damageCount > 0 && groups[state.groupIndex] == state.damageCount) {
                                arr += count(state.next(spring = true, group = true))
                            } else if (state.damageCount == 0) {
                                arr += count(state.next(spring = true))
                            }
                        }
                    }
                    when (springs[state.springIndex]) {
                        '?', '#' -> {
                            if (state.groupIndex < groups.size && state.damageCount < groups[state.groupIndex]) {
                                arr += count(state.next(spring = true, damage = true))
                            }
                        }
                    }
                    cacheForState[state] = arr
                    arr
                }
            }
        }
    }
    return count(CounterState())
}

fun main() {
    fun part1(input: List<String>) = input.sumOf { countArrangements(row = it) }

    fun part2(input: List<String>) = input.sumOf { countArrangements(row = it, foldFactor = 5) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2023, "Day12_test")
    compare(part1(testInput), 21)
    compare(part2(listOf(".??..??...?##. 1,1,3")), 16384)
    compare(part2(testInput), 525152)

    // apply on real input
    val input = readInput(2023, "Day12")
    boldPrint("\nPart 1: ${part1(input)}\n")
    boldPrint("\nPart 2: ${part2(input)}\n")
}