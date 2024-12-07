package y2024

import readInput
import utils.compare
import kotlin.streams.toList

val operators = listOf('*', '+')
val extendedOperators = operators + '|'

object Day07 {
    fun part1(input: List<String>): Long {
        val equations = parse(input)

        val workingEquationsResult = equations.mapNotNull { (result, operands) ->
            val workingEquations = operands.fold(listOf(result to "")) { acc, operand ->
                if (acc.size == 1 && acc[0].second == "") listOf(result to "$operand")
                else {
                    acc.flatMap { (result, operation) ->
                        operators.map { operator ->
                            result to "$operation $operator $operand"
                        }
                    }
                }
            }
                .stream()
                .parallel()
                .filter { (result, operation) ->
                    var acc = 0L
                    var operator = '+'

                    for (symbol in operation.split(" ")) {
                        if (symbol[0] in operators) {
                            operator = symbol[0]
                        } else {
                            when (operator) {
                                '*' -> acc *= symbol.toLong()
                                '+' -> acc += symbol.toLong()
                                else -> error("Unknown symbol")
                            }
                        }
                        if (acc > result) return@filter false
                        else if (acc == result) return@filter true
                    }

                    false
                }.toList()

            if (workingEquations.isNotEmpty()) result
            else null
        }

        println(workingEquationsResult)

        return workingEquationsResult.sum()
    }

    fun part2(input: List<String>): Long {
        val equations = parse(input)

        val workingEquationsResult = equations
            .mapNotNull { (result, operands) ->
                val workingEquations = operands.fold(listOf(result to "")) { acc, operand ->
                    if (acc.size == 1 && acc[0].second == "") listOf(result to "$operand")
                    else {
                        acc.flatMap { (result, operation) ->
                            extendedOperators.map { operator ->
                                result to "$operation $operator $operand"
                            }
                        }
                    }
                }
                    .stream()
                    .parallel()
                    .filter { (result, operation) ->
                        var acc = 0L
                        var operator = '+'

                        for (symbol in operation.split(" ")) {
                            if (symbol[0] in extendedOperators) {
                                operator = symbol[0]
                            } else {
                                when (operator) {
                                    '*' -> acc *= symbol.toLong()
                                    '+' -> acc += symbol.toLong()
                                    '|' -> acc = "$acc$symbol".toLong()
                                    else -> error("Unknown symbol: $operator")
                                }
                            }

                            if (acc > result) return@filter false
                        }

                        acc == result
                    }
                    .toList()

                if (workingEquations.isNotEmpty()) {
                    println("Found working equations for $result:")
                    println(workingEquations)
                    result
                } else null
            }

        println(workingEquationsResult)
        return workingEquationsResult.sum()
    }

    private fun parse(input: List<String>): List<Pair<Long, List<Long>>> {
        val equations = input.map {
            val (result, operands) = it.split(": ")
            result.toLong() to operands.split(" ").map { it.toLong() }
        }
        return equations
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day07_test")
    compare(Day07.part1(testInput), 3749L)
    compare(Day07.part2(testInput), 11387L)

    // apply on real input
    val input = readInput(2024, "Day07")
    println("Part 1: ${Day07.part1(input)}")
    println("Part 2: ${Day07.part2(input)}")
}
