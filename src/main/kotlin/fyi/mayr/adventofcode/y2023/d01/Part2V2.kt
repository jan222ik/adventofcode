package fyi.mayr.adventofcode.y2023.d01

import java.io.File

fun main(args: Array<String>) {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2023/d01/input.txt").readLines()
    part2(lines)
}

private fun part2(lines: List<String>) {
    val sum = lines.sumOf(::extractNumber)
    println("Part 2V2: sum = ${sum}")
}

private val numberNames = mutableMapOf(
    "one" to 1, "two" to 2, "three" to 3,
    "four" to 4, "five" to 5, "six" to 6,
    "seven" to 7, "eight" to 8, "nine" to 9
).also {
    (1..9).forEach { num -> it[num.toString()] = num }
}

private fun extractNumber(input: String) : Int {
    return firstMatchDigit(input) * 10 + firstMatchDigit(input.reversed(), numberNames.mapKeys { it.key.reversed() })
}

private fun firstMatchDigit(input: String, words: Map<String, Int> = numberNames) : Int {
    return input.findAnyOf(words.keys)?.let { words[it.second] } ?: error("")
}

