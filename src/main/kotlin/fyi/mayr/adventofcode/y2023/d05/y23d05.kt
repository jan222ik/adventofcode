package fyi.mayr.adventofcode.y2023.d05

import fyi.mayr.adventofcode.util.adventOfCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() = adventOfCode(2023, 5) {
    val lines = defaultInputFile.readLines()
    val (seeds, mappingTables) = parse(lines)
    part1 {
        seeds.minOf { lookup("seed", "location", it, mappingTables) }
    }

    part2("slow") { // Set more memory, e.g -Xmx6g
        val windowed = seeds.windowed(2, 2) { (start, range) ->
            start..<start + range
        }
        runBlocking {
            windowed.map { range ->
                async(Dispatchers.IO) {
                    var min = Long.MAX_VALUE
                    for (l in range) {
                        val lookup = lookup("seed", "location", l, mappingTables)
                        if (lookup < min) {
                            min = lookup
                        }
                    }
                    min
                }
            }.awaitAll().min()
        }

        // 60 GB memory not enough :/
        //.minOf { lookup("seed", "location", it, mappingTables) }
    }
}

private fun lookup(source: String, target: String, input: Long, mappingTables: List<MappingTable>): Long {
    if (source == target) return input
    val mappingTable = mappingTables.find { it.source == source } ?: error("There should be a table")
    val output = (mappingTable.mappings.find { it.sourceRange.contains(input) }?.difference ?: 0) + input
    return lookup(mappingTable.target, target, output, mappingTables)
}

private data class Mapping(val range: Long, val sourceStart: Long, val targetStart: Long) {
    val sourceRange = sourceStart.rangeUntil(sourceStart + range)
    val difference = targetStart - sourceStart
}

private data class MappingTable(val source: String, val target: String, val mappings: List<Mapping>)

private fun parse(lines: List<String>): Pair<List<Long>, List<MappingTable>> {
    val seeds = lines.first().split(": ").last().split(' ').map { it.toLong() }
    return seeds to buildList<MappingTable> {
        var currentMap = ""
        var currentMappings = mutableListOf<Mapping>()
        val saveList = {
            if (currentMap.isNotEmpty()) {
                val (source, target) = currentMap.split("-to-")
                add(MappingTable(source, target, currentMappings))
                currentMappings = mutableListOf()
            }
        }
        for (line in lines.drop(1)) {
            if (line.isEmpty()) continue
            if (line.contains("map")) {
                saveList()
                currentMap = line.split(" ").first().also { println(it) }
                continue
            }
            val (destination, source, range) = line.split(" ").map { it.toLong() }
            currentMappings.add(Mapping(range, source, destination))
        }
        saveList()
    }
}
