package fyi.mayr.adventofcode.y2023.d01

import java.io.File

fun main(args: Array<String>) {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2023/d01/input.txt").readLines()
    part1(lines)
}

fun part1(lines: List<String>) {
    val sum = lines
        .map(::extractNumber)
        .sum()
    println("Part 1: sum = ${sum}")
}

private fun extractNumber(input: String) : Int {
    return input.first(Char::isDigit).digitToInt().times(10)
        .plus(input.last(Char::isDigit).digitToInt())
}
