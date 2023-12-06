package fyi.mayr.adventofcode.y2023.d06

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 6) {
    val lines = defaultInputFile.readLines()
    val linesWithoutLabel = lines.map { it.split(":").last().trim() }
    part1 {
        val (times, distances) = linesWithoutLabel.map { it.split(" ").mapNotNull(String::toLongOrNull) }
        times.zip(distances, ::winPossibilitiesAgainstRecord).fold(1, Int::times)
    }

    part2 {
        val (time, distance) = linesWithoutLabel.map { it.replace(" ", "").toLong() }
        winPossibilitiesAgainstRecord(time, distance)
    }
}

private fun winPossibilitiesAgainstRecord(totalTime: Long, distance: Long): Int =
    (1..<totalTime).count { chargeTime -> distance < chargeTime * (totalTime - chargeTime) }

