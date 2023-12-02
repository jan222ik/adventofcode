package fyi.mayr.adventofcode.y2023.d02

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2023/d02/input.txt").readLines()
    val limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )
    println("Day 2 - Part 1:" + part1(lines, limits))
}
private fun part1(lines: List<String>, limits: Map<String, Int>) : Int = lines.sumOf { idIfPossibleOrNull(it, limits) ?: 0 }

private val REGEX_GAME_ID = "Game (?<id>\\d+):".toRegex()

private fun idIfPossibleOrNull(s: String, limits: Map<String, Int>) : Int? {
    println("id=${REGEX_GAME_ID.find(s)?.groups?.get("id")?.value?.toInt()}, s = ${s}")
    for ((color, limit) in limits) {
        val regex = "(?<$color>\\d+) $color".toRegex()
        s.split(";")
            .forEach {
                val count = regex
                    .findAll(it)
                    .fold(0) { acc, matchResult -> acc + (matchResult.groups[color]?.value?.toInt() ?: 0) }
                println("color = ${color} count = ${count} limit = ${limit}, ${count > limit}")
                if (count > limit) {
                    return null
                }
            }
    }
    return REGEX_GAME_ID.find(s)?.groups?.get("id")?.value?.toInt()?.also { println("id $it") }
}
