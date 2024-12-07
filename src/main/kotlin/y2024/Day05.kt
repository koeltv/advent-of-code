package y2024

import readInput
import utils.compare

object Day05 {
    private fun parse(input: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val (rules, updates) = input.filter { it.isNotBlank() }.partition { it.contains('|') }

        val orderingRules = rules
            .map { it.split('|') }
            .map { (left, right) -> left.toInt() to right.toInt() }
            .groupBy({ (left, _) -> left }) { (_, right) -> right }

        val updateLists = updates.map { list -> list.split(',').map { it.toInt() } }
        return Pair(orderingRules, updateLists)
    }

    fun part1(input: List<String>): Int {
        val (orderingRules, updateLists) = parse(input)

        val temp = updateLists
            .filter { updateList ->
                val prevItems = mutableListOf<Int>()
                for (item in updateList) {
                    if (orderingRules[item] != null && orderingRules[item]!!.any { postItem -> postItem in prevItems }) {
                        return@filter false
                    }
                    prevItems += item
                }
                return@filter true
            }

        return temp.map {
            it[it.size / 2]
        }.sum()
    }

    fun isSorted(list: List<Int>, orderingRules: Map<Int, List<Int>>): Boolean {
        val prevItems = mutableListOf<Int>()
        for (item in list) {
            if (orderingRules[item] != null && orderingRules[item]!!.any { postItem -> postItem in prevItems }) {
                return false
            }
            prevItems += item
        }
        return true
    }

    fun sortList(list: List<Int>, orderingRules: Map<Int, List<Int>>): List<Int> {
        val prevItems = mutableListOf<Int>()
        for ((i, item) in list.withIndex()) {
            if (orderingRules[item] != null && orderingRules[item]!!.any { postItem -> postItem in prevItems }) {
                val prevItem = prevItems.removeLast()
                return sortList(prevItems + item + prevItem + list.subList(i + 1, list.size), orderingRules)
            }
            prevItems += item
        }
        return list
    }

    fun part2(input: List<String>): Int {
        val (orderingRules, updateLists) = parse(input)

        return updateLists
            .filter { !isSorted(it, orderingRules) }
            .map { sortList(it, orderingRules) }
            .map { it[it.size / 2] }
            .sum()
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day05_test")
    compare(Day05.part1(testInput), 143)
    compare(Day05.part2(testInput), 123)

    // apply on real input
    val input = readInput(2024, "Day05")
    println("Part 1: ${Day05.part1(input)}")
    println("Part 2: ${Day05.part2(input)}")
}
