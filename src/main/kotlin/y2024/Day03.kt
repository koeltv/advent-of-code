package y2024

import readInput
import utils.compare

private sealed class Instruction
private data class Mul(val leftOperand: Int, val rightOperand: Int) : Instruction() {
    fun compute(): Int = leftOperand * rightOperand
}

private data object Do : Instruction()
private data object Dont : Instruction()

object Day03 {
    fun part1(input: List<String>): Int {
        return input.flatMap { line ->
            Regex("mul\\((\\d+),(\\d+)\\)").findAll(line).map { match ->
                match.destructured.let { (leftOperand, rightOperand) -> leftOperand.toInt() to rightOperand.toInt() }
            }
        }.sumOf { (l, r) -> l * r }
    }

    fun part2(input: List<String>): Int {
        return input.flatMap { line ->
            Regex("(mul\\((\\d+),(\\d+)\\))|(do\\(\\))|(don't\\(\\))").findAll(line).map { match ->
                if ("mul" in match.value) Mul(match.groupValues[2].toInt(), match.groupValues[3].toInt())
                else if ("don't" in match.value) Dont
                else Do
            }
        }.fold(true to 0) { (enabled, sum), instruction ->
            when (instruction) {
                Do -> true to sum
                Dont -> false to sum
                is Mul -> if (enabled) true to sum + instruction.compute() else false to sum
            }
        }.second
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2024, "Day03_test")
    val testInput2 = readInput(2024, "Day03_test2")
    compare(Day03.part1(testInput), 161)
    compare(Day03.part2(testInput2), 48)

    // apply on real input
    val input = readInput(2024, "Day03")
    println("Part 1: ${Day03.part1(input)}")
    println("Part 2: ${Day03.part2(input)}")
}
