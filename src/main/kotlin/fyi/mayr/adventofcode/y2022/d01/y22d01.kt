package fyi.mayr.adventofcode.y2022.d01

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2022, 1) {
    val lines = defaultInputFile.readLines()
    part1 {
        caloriesPerElf(lines).max()
    }
    part2 {
        caloriesPerElf(lines).apply { sortDescending() }.take(3).sum()
    }
}.run()

private fun caloriesPerElf(lines: List<String>): MutableList<Int> {
    return lines.asSequence()
        .map { it.toIntOrNull() }
        .fold(mutableListOf(0)) { elfs, calorieOrNull ->
            if (calorieOrNull == null) {
                elfs.add(0)
            } else {
                elfs[elfs.lastIndex] += calorieOrNull
            }
            elfs
        }
}
