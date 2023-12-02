package fyi.mayr.adventofcode.y2022.d01

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2022/d01/input.txt").readLines()
    println("Day 1 - Part 1: " + part1(lines))
    println("Day 1 - Part 2: " + part2(lines))
}

private fun caloriesPerElf(lines: List<String>) : MutableList<Int> {
    var sum = 0
    val elfs = mutableListOf<Int>();
    for (line in lines) {
        if (line.isEmpty()) {
            elfs.add(sum)
            sum = 0
        } else {
            sum += line.toInt()
        }
    }
    return elfs
}

private fun caloriesPerElfStream(lines: List<String>) : MutableList<Int> {
    return lines.asSequence()
        .map { it.toIntOrNull() }
        .fold(mutableListOf(0)) {elfs, value ->
            if (value == null) {
                elfs.add(0)
            } else {
                elfs[elfs.lastIndex] +=value
            }
            elfs
        }
}

private fun part1(lines: List<String>) : Int {
    return caloriesPerElf(lines).max()
}

private fun part2(lines: List<String>) : Int {
    return caloriesPerElfStream(lines).apply { sortDescending() }.take(3).sum()
}
