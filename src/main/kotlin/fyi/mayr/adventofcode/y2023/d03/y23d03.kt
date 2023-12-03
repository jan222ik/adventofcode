package fyi.mayr.adventofcode.y2023.d03

import fyi.mayr.adventofcode.util.adventOfCode


fun main() = adventOfCode(2023, 3) {
    val lines = defaultInputFile.readLines()
    val engineLines = parse(lines)
    part1 {
        getValidPartNumbers(engineLines)
            .sumOf { validPartsBySymbol -> validPartsBySymbol.values.sumOf { parts -> parts.sumOf { it.value.toInt() } } }
    }

    part2 {
        getValidPartNumbers(engineLines)
            .sumOf { validPartsBySymbol ->
                validPartsBySymbol
                    .filterKeys { it.value == "*" }
                    .filterValues { it.size == 2 }
                    .values
                    .map { parts -> parts.map { it.value.toInt() } }
                    .sumOf { parts -> parts.first() * parts.last() }
            }
    }

}

private fun getValidPartNumbers(engineLines: List<EngineLine>): List<Map<Location, List<Location>>> {
    return engineLines.windowed(3, step = 1, partialWindows = true) w@{ el ->
        if (el.size < 2) {
            return@w emptyMap()
        }
        val (top, middle) = el
        val bottom = el.getOrNull(2)

        middle.symbols.associateWith { symbolLoc ->
            val searchList = top.engineParts.plus(middle.engineParts) + (bottom?.engineParts ?: emptyList())
            searchList.filter { location ->
                location.range.intersect(symbolLoc.searchRange.toList().toSet()).isNotEmpty()
            }
        }
    }
}

private data class Location(val range: IntRange, val value: String) {
    val searchRange: IntRange
        get() = IntRange(range.first.minus(1), range.first.plus(1))
}

private data class EngineLine(val symbols: List<Location>, val engineParts: List<Location>)

private fun parse(lines: List<String>): List<EngineLine> {
    return lines.map { line ->
        val engineParts = mutableListOf<Location>()
        val symbols = mutableListOf<Location>()
        var runningPartNr = ""
        var partStartIdx: Int? = null
        line.forEachIndexed { index, c ->
            if (c.isDigit()) {
                if (partStartIdx == null) {
                    partStartIdx = index
                }
                runningPartNr += c
            } else {
                partStartIdx?.let {
                    engineParts.add(Location(IntRange(it, index - 1), runningPartNr))
                    runningPartNr = ""
                    partStartIdx = null
                }
                if (c != '.') { // Any symbol
                    symbols.add(Location(IntRange(index, index), c.toString()))
                }
            }
            if (index == line.lastIndex) {
                partStartIdx?.let {
                    engineParts.add(Location(IntRange(it, index - 1), runningPartNr))
                    runningPartNr = ""
                    partStartIdx = null
                }
            }
        }
        EngineLine(symbols, engineParts)
    }
}
