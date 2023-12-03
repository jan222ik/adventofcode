package fyi.mayr.adventofcode.y2023.d01

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 3) {
    val lines = defaultInputFile.readLines()
    part2("V2", scope = Part2V2) {
        lines.sumOf { extractNumber(it) }
    }
}

private object Part2V2 {

    private val numberNames = mutableMapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9
    ).also {
        (1..9).forEach { num -> it[num.toString()] = num }
    }

    fun extractNumber(input: String): Int {
        return firstMatchDigit(input) * 10 + firstMatchDigit(
            input.reversed(),
            numberNames.mapKeys { it.key.reversed() })
    }

    private fun firstMatchDigit(input: String, words: Map<String, Int> = numberNames): Int {
        return input.findAnyOf(words.keys)?.let { words[it.second] } ?: error("")
    }
}

