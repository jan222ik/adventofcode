package fyi.mayr.adventofcode.util

import java.io.File


@DslMarker
annotation class AdventOfCodeDsl

interface AdventOfCodeScope {
    fun part(name: String, block: () -> Any?)
    fun part1(suffix: String = "", block: () -> Any?) = part("1$suffix", block)
    fun part2(suffix: String = "", block: () -> Any?) = part("2$suffix", block)
    val defaultInputFile : File
}
@AdventOfCodeDsl
class AdventOfCodeScopeImpl(private val year: Int, day: Int) : AdventOfCodeScope {
    private val partNames = mutableSetOf<String>()
    private val parts = mutableListOf<Pair<String, () -> Any?>>()

    private val dayPadded = day.toString().padStart(2, '0')

    private fun add(name: String, block: () -> Any?) {
        if (partNames.contains(name)) {
            throw IllegalStateException("Only one part block with name $name is allowed.")
        }
        partNames.add(name)
        parts.add(name to block)
    }

    override fun part(name: String, block: () -> Any?) = add(name, block)
    override val defaultInputFile: File
        get() = File("src/main/kotlin/fyi/mayr/adventofcode/y$year/d$dayPadded/input.txt")

    fun execute() {
        parts.forEach {
            println("Y$year - Day $dayPadded - Part ${it.first}: ${it.second().toString()}")
        }
    }
}

@AdventOfCodeDsl
fun adventOfCode(year: Int, day: Int, block: AdventOfCodeScope.() -> Unit): Runnable {
    val scope = AdventOfCodeScopeImpl(year, day)
    block(scope)
    return Runnable{ scope.execute() }
}

