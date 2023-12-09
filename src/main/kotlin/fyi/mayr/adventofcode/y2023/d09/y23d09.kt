package fyi.mayr.adventofcode.y2023.d09

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 9) {
    val lines = defaultInputFile.readLines()
        .map { it.split(" ").map(String::toInt) }

    part1 {
        lines.sumOf(::determineNextSequenceNumber)
    }

    part2 {
        lines.map(List<Int>::reversed)
            .sumOf(::determineNextSequenceNumber)
    }
}

private fun determineNextSequenceNumber(sequence: List<Int>): Int {
    var curDiff: Int? = null
    var allSame = true
    val differences = sequence.windowed(2) { (l, r) ->
        val diff = r - l
        if (allSame) {
            if (diff != (curDiff ?: diff)) {
                allSame = false
            } else {
                curDiff = diff
            }
        }
        diff
    }
    return sequence.last() + when {
        allSame -> differences.last()
        else -> determineNextSequenceNumber(differences)
    }
}
