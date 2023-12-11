package fyi.mayr.adventofcode.y2023.d10

import fyi.mayr.adventofcode.util.adventOfCode
import fyi.mayr.adventofcode.y2023.d10.Direction.*

fun main() = adventOfCode(2023, 10) {
    val lines = defaultInputFile.readLines()

    part1 {
        var start: Pair<Int, Int>? = null
        for ((idx, line) in lines.withIndex()) {
            line.indexOf('S').takeUnless { it == -1 }?.let { start = idx to it }
            if (start != null) {
                break
            }
        }
        println("start = ${start}")
        val seen = mutableSetOf<Pair<Int, Int>>()
        val expand = mutableListOf(start!! to 0)
        var lastCost: Int = 0
        while (expand.isNotEmpty()) {
            val (pair, cost) = expand.removeFirst()
            println("cost = ${cost}")
            lastCost = cost
            seen.add(pair)
            val (x, y) = pair
            val neighbours = getNeighbours(x, y, lines)
                .filterNot { seen.contains(it) }
                .map { it to cost + 1 }
            expand.addAll(expand.lastIndex + 1, neighbours)
        }
        lastCost
    }
}

private fun getNeighbours(x: Int, y: Int, lines: List<String>): List<Pair<Int, Int>> {
    val currentTileExpandDirections = getTileOpenings(lines[x][y]).also { println("currentTileExpandDirections = ${it} for ${lines[x][y]} ($x, $y)") }
    return currentTileExpandDirections
        .filter { direction ->
            println("\ndirection = ${direction}")
            lines.getOrNull(x + direction.dRow)?.getOrNull(y + direction.dCol).also { println("char = ${it}, ${x + direction.dRow}, ${y + direction.dCol}") }
                ?.let {c ->
                    getTileOpenings(c).also { println("adjTileExpandDirections = ${it} for $c") }.contains(direction.opposite()).also { println("c: $c, dir ${direction.opposite()} $it") }
                } == true
        }
        .also { println("x = $x, y = $y, c = ${lines[x][y]}, d = $currentTileExpandDirections to = $it") }
        .map { direction -> x + direction.dRow to y + direction.dCol }

}

private fun getTileOpenings(c: Char): List<Direction> {
    return when (c) {
        '|' -> listOf(N, S) // |
        '-' -> listOf(E, W) // -
        'L' -> listOf(N, E) // └
        'J' -> listOf(N, W) // ┘
        '7' -> listOf(S, W) // ┐
        'F' -> listOf(S, E) // ┌
        '.' -> emptyList()
        'S' -> listOf(N, E, S, W)
        else -> error("should not expand to this node $c")
    }
}

/*
      | - 1 | 0 | + 1 x
   -1 |       N
    0 |  W         E
   +1 |       S
    y
*/
enum class Direction(val dCol: Int, val dRow: Int) {
    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0);

    fun opposite() = when (this) {
        N -> S
        E -> W
        S -> N
        W -> E
    }
}

