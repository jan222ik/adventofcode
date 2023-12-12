package fyi.mayr.adventofcode.y2023.d11

import fyi.mayr.adventofcode.util.adventOfCode
import kotlin.math.abs

fun main() = adventOfCode(2023, 11) {
    val lines = defaultInputFile.readLines()

    part1("Expanding the Input") {
        println("lines = [${lines.size},${lines[0].length}]\n" + lines.joinToString("\n"))
        val expanded = expand(lines)
        println("expanded = [${expanded.size},${expanded[0].length}]\n${expanded.joinToString("\n")}")
        val positions = findGalaxyPositions(expanded)
        val combinations = getCombinations(positions)
        println("combinations = ${combinations.size}")
        val map = combinations.map {
            val (start, destination) = it
            it to manhattanDistance(start, destination)
        }
        map.sumOf { it.second }
    }

    part1 {
        findGalaxyPositions(lines)
            .getCombinations()
            .sumOf(
                calculateMinDistance(
                    determineExpandedRowIndices(lines),
                    determineExpandedColIndices(lines),
                    3
                )
            )
    }

    part2 {
        findGalaxyPositions(lines)
            .getCombinations()
            .sumOf(
                calculateMinDistance(
                    determineExpandedRowIndices(lines),
                    determineExpandedColIndices(lines),
                    1_000_000
                )
            )
    }
}

private fun determineExpandedColIndices(lines: List<String>) =
    lines.first().indices.filter { idx -> lines.all { s -> s[idx] == '.' } }

private fun determineExpandedRowIndices(lines: List<String>) =
    lines.mapIndexedNotNull { idx, s -> idx.takeIf { s.all { c -> c == '.' } } }

private fun calculateMinDistance(
    expandRows: List<Int>,
    expandCols: List<Int>,
    expansionFactor: Int
): (Pair<Point, Point>) -> Long = { (start, destination) ->
    val rowRange = listOf(start.row, destination.row).sorted().zipWithNext { a, b -> a..b }.first()
    val colRange = listOf(start.col, destination.col).sorted().zipWithNext { a, b -> a..b }.first()
    manhattanDistance(start, destination).toLong() +
            expandRows.count { idx -> rowRange.contains(idx) }.toLong().times(expansionFactor - 1) +
            expandCols.count { idx -> colRange.contains(idx) }.toLong().times(expansionFactor - 1)
}

private fun List<Point>.getCombinations() = getCombinations(this)

private fun getCombinations(positions: List<Point>): List<Pair<Point, Point>> {
    return buildList {
        for (i in 0..<positions.size - 1) {
            for (j in i + 1..<positions.size) {
                add(Pair(positions[i], positions[j]))
            }
        }
    }
}

private fun findGalaxyPositions(expanded: List<String>) = expanded.mapIndexedNotNull { rowIdx, line ->
    line.mapIndexedNotNull { colIdx, c -> Point(colIdx, rowIdx).takeIf { c == '#' } }.takeIf { it.isNotEmpty() }
}.flatten()

data class Point(val col: Int, val row: Int) {
    override fun toString(): String {
        return "{r$row,c$col}"
    }
}

fun manhattanDistance(start: Point, destination: Point): Int {
    return abs(destination.col - start.col) + abs(destination.row - start.row)
}

private fun expand(lines: List<String>): List<String> {
    return buildList {
        for (line in lines) {
            if (line.all { it == '.' }) {
                add(line)
            }
            add(line)
        }
    }.let { rowExpanded ->
        val indices = buildList {
            for (idx in 0..rowExpanded[0].lastIndex) {
                if (rowExpanded.all { it[idx] == '.' }) {
                    add(idx)
                }
            }
        }
        rowExpanded.map {
            it.toMutableList().apply { indices.forEachIndexed { idx, it -> add(it + idx + 1, '.') } }.joinToString("")
        }
    }
}
