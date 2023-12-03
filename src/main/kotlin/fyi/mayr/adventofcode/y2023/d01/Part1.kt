package fyi.mayr.adventofcode.y2023.d01

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 1) {
    val lines = defaultInputFile.readLines()
    part1 {
        lines.sumOf { extractNumber(it) }
    }
}

private fun extractNumber(input: String): Int {
    return input.first(Char::isDigit).digitToInt().times(10)
        .plus(input.last(Char::isDigit).digitToInt())
}
