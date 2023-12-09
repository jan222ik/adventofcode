package fyi.mayr.adventofcode.y2023.d08

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 8) {
    val lines = defaultInputFile.readLines()

    part1 {
        solvePart1(parse(lines))
    }

    part2 {
        part2(parse(lines))
        //solvePart2(parse(lines)) -> very, very long runtime
    }
}


private fun solvePart2(input: Pair<String, List<Node>>): Long {
    val (directions, inNodes) = input
    val nodes = inNodes.associateBy { it.name }
    var currentNodes = inNodes.filter { node -> node.name.last() == 'A' }
    var index: Long = 0
    while (!currentNodes.all { node -> node.name.last() == 'Z' }) {
        val direction = directions[(index % directions.length).toInt()]
        currentNodes = currentNodes.map { node ->
            val nextNode = when (direction) {
                'R' -> node.right
                'L' -> node.left
                else -> error("Should not happen")
            }
            nodes[nextNode]!!
        }
        index += 1
    }
    return index
}

private fun solvePart1(input: Pair<String, List<Node>>): Int {
    val (directions, inNodes) = input
    val nodes = inNodes.associateBy { it.name }
    var currentNode = nodes["AAA"]!!
    var index = 0
    while (currentNode.name != "ZZZ") {
        val direction = directions[index % directions.length]
        val nextNode = when (direction) {
            'R' -> currentNode.right
            'L' -> currentNode.left
            else -> error("Should not happen")
        }
        currentNode = nodes[nextNode]!!
        index += 1
    }
    return index
}

data class Node(val name: String, val left: String, val right: String)

private fun parse(lines: List<String>): Pair<String, List<Node>> {
    val directions = lines.first()
    return directions to lines.drop(2)
        .map { line -> line.split(" = ") }
        .map {
            val (left, right) = it.last().trim('(', ')').split(", ")
            Node(it.first(), left, right)
        }
}

private fun part2(input: Pair<String, List<Node>>): Long {
    val (directions, inNodes) = input
    val nodes = inNodes.associateBy { it.name }
    fun step(start: String) = directions.fold(start) { acc, char ->
        when (char) {
            'L' -> nodes[acc]!!.left
            'R' -> nodes[acc]!!.right
            else -> throw IllegalStateException()
        }
    }

    return directions.length *
            nodes.keys.filter { it.endsWith('A') }.map { start ->
                generateSequence(start, ::step).indexOfFirst { it.endsWith('Z') }
            }.fold(1L) { x, y -> lcm(x, y.toLong()) }
}


private fun gcd(x: Long, y: Long): Long {
    var a = x
    var b = y
    while (b != 0L) a = b.also { b = a.mod(b) }
    return a
}

private fun lcm(x: Long, y: Long): Long = x / gcd(x, y) * y
