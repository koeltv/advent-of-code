# Advent of Code

Welcome to the Advent of Code[^aoc] project created by [koeltv][github].

In this repository, you will find solutions to the puzzles over different years.

This repository contains utilities to make testing and benchmarking your solutions easier.

## Gradle Commands

Create a new package for a year:
```bash
./gradlew newYear [-Pyear]
```
You can optionally pass the year you want, otherwise it default to the ongoing year.

Create the source file, test file and empty input files for the next day of the latest year:
```bash
./gradlew newDay [-Pyear] [-Pday] [-Pbenchmark]
```
You can optionally pass the following arguments:
- `year` to specify which year, default to the latest one
- `day` to specify which day, default to the last existing + 1
- `benchmark` (no value needed) to also create a benchmark for the day.

Create a benchmark file for the given day of the given year
```bash
./gradlew newBenchmark -Pyear=<YEAR> -Pday=<DAY>
```

Run benchmarks with [JMH][jmh]:
```bash
./gradlew jmh [-Pselect=<CRITERIAS>]
```
`CRITERIAS` being a comma-separated list of RegExp conditions.
The RegExp applies to the whole path of the benchmark methods, including files (ex: `Day01Benchmark`), packages (ex: `y2023`) and methods (ex: `benchmarkPart1`), so you can:
- use `Part1` to run all Part1 methods,
- use `2023,Part2` to run all Part2 methods from year 2023, 
- use `2023,Day12,Part2` to run all Part2 methods from day 12 of year 2023.

[^aoc]:
[Advent of Code][aoc] – An annual event of Christmas-oriented programming challenges started December 2015.
Every year since then, beginning on the first day of December, a programming puzzle is published every day for twenty-five days.
You can solve the puzzle and provide an answer using the language of your choice.

[aoc]: https://adventofcode.com
[github]: https://github.com/koeltv
[jmh]: https://github.com/openjdk/jmh