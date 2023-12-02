package fyi.mayr.adventofcode.y2023.d02

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2023/d02/input.txt").readLines()
    val colors = listOf("red", "green", "blue")
    println("Day 2 - Part 2: " + part2(lines, colors))
}
private fun part2(lines: List<String>, colors: List<String>) : Int = lines.sumOf { powerOfGame(it, colors) }

const val MUL_NEUTRAL = 1
private fun powerOfGame(s: String, colors: List<String>) : Int {

    return colors.fold(MUL_NEUTRAL) { acc: Int, color: String ->
        val regex = "(?<$color>\\d+) $color".toRegex()
        val max = s.split(";")
            .maxOfOrNull {
                regex.find(it)?.groups?.get(color)?.value?.toInt() ?: MUL_NEUTRAL
            } ?: MUL_NEUTRAL
        acc * max
    }
}
