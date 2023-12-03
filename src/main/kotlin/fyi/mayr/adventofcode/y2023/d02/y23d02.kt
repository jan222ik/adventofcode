package fyi.mayr.adventofcode.y2023.d02

import fyi.mayr.adventofcode.util.adventOfCode
import fyi.mayr.adventofcode.y2023.d02.Part1.idIfPossibleOrNull
import fyi.mayr.adventofcode.y2023.d02.Part2.powerOfGame

fun main() = adventOfCode(2023, 2) {
    val lines = defaultInputFile.readLines()
    part1 {
        val limits = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        lines.sumOf { idIfPossibleOrNull(it, limits) ?: 0 }
    }
    part2 {
        val colors = listOf("red", "green", "blue")
        lines.sumOf { powerOfGame(it, colors) }
    }
}.run()

private object Part1 {
    private val REGEX_GAME_ID = "Game (?<id>\\d+):".toRegex()

    fun idIfPossibleOrNull(s: String, limits: Map<String, Int>): Int? {
        for ((color, limit) in limits) {
            val regex = "(?<$color>\\d+) $color".toRegex()
            s.split(";")
                .forEach {
                    val count = regex
                        .findAll(it)
                        .fold(0) { acc, matchResult -> acc + (matchResult.groups[color]?.value?.toInt() ?: 0) }
                    if (count > limit) {
                        return null
                    }
                }
        }
        return REGEX_GAME_ID.find(s)?.groups?.get("id")?.value?.toInt()
    }
}

private object Part2 {

    const val MUL_NEUTRAL = 1

    fun powerOfGame(s: String, colors: List<String>): Int {
        return colors.fold(MUL_NEUTRAL) { acc: Int, color: String ->
            val regex = "(?<$color>\\d+) $color".toRegex()
            val max = s.split(";")
                .maxOfOrNull {
                    regex.find(it)?.groups?.get(color)?.value?.toInt() ?: MUL_NEUTRAL
                } ?: MUL_NEUTRAL
            acc * max
        }
    }
}
