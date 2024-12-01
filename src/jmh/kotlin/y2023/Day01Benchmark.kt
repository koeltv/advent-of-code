package y2023

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import readInput

@State(Scope.Benchmark)
open class Day01Benchmark {
    private val input = readInput(2023, "Day01")

    @Benchmark
    fun benchmarkPart1() {
        Day01.part1(input)
    }

    @Benchmark
    fun benchmarkPart2() {
        Day01.part2(input)
    }
}
