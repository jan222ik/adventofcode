package fyi.mayr.adventofcode.y2022.d02

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2022/d02/input.txt").readLines()
    val inputMapping = mapOf(
        'A' to Gesture.Rock,
        'B' to Gesture.Paper,
        'C' to Gesture.Scissors
    )
    val strategyMappingPart1 = mapOf(
        'Y' to Gesture.Paper,
        'X' to Gesture.Rock,
        'Z' to Gesture.Scissors
    )
    val strategyMappingPart2 = mapOf<Char, (Gesture) -> Gesture>(
        'X' to { g -> g.winAgainst() }, // Should lose, so get the one the input wins against
        'Y' to { g -> g.drawAgainst() },
        'Z' to { g -> g.lossAgainst() } // Should win, so get the one the input loses against
    )
    val part1 = parse(lines = lines,
        inputMatcher = { inputMapping[it]!! },
        outputMatcher = { c, _ -> strategyMappingPart1[c]!! }).sumOf { it.score }
    println("y22d02p1: $part1")
    val part2 = parse(lines = lines,
        inputMatcher = { inputMapping[it]!! },
        outputMatcher = { c, g -> strategyMappingPart2[c]!!.invoke(g) }).sumOf { it.score }
    println("y22d02p2: $part2")
}

private enum class Gesture(val score: Int) {
    Rock(1), Paper(2), Scissors(3);

    fun winAgainst() = when (this) {
        Rock -> Scissors
        Paper -> Rock
        Scissors -> Paper
    }

    fun drawAgainst() = this

    fun lossAgainst() = when (this) {
        Rock -> Paper
        Paper -> Scissors
        Scissors -> Rock
    }
}

private const val SCORE_WIN = 6
private const val SCORE_DRAW = 3
private const val SCORE_LOSS = 0

private data class Round(val actual: Gesture, val response: Gesture) {
    val score
        get() = response.score + when (actual) {
            response.winAgainst() -> SCORE_WIN
            response.drawAgainst() -> SCORE_DRAW
            response.lossAgainst() -> SCORE_LOSS
            else -> error("Unreachable")
        }
}

private fun parse(
    lines: List<String>,
    inputMatcher: (c: Char) -> Gesture,
    outputMatcher: (c: Char, input: Gesture) -> Gesture
): List<Round> {
    return lines.map {
        val actual = inputMatcher(it.first())
        Round(actual, outputMatcher(it.last(), actual))
    }
}

