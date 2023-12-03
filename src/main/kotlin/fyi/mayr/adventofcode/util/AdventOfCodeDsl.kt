package fyi.mayr.adventofcode.util

import java.io.File


@DslMarker
annotation class AdventOfCodeDsl

interface AdventOfCodeScope {
    fun part(name: String, block: () -> Any?)
    fun part1(suffix: String = "", block: () -> Any?) = part("1$suffix", block)
    fun part2(suffix: String = "", block: () -> Any?) = part("2$suffix", block)

    fun <T> part(name: String, scope: T, block: T.() -> Any?)
    fun <T> part1(suffix: String = "", scope: T, block: T.() -> Any?) = part("1$suffix", scope, block)
    fun <T> part2(suffix: String = "", scope: T, block: T.() -> Any?) = part("2$suffix", scope, block)
    val defaultInputFile : File
}
@AdventOfCodeDsl
class AdventOfCodeScopeImpl(private val year: Int, day: Int) : AdventOfCodeScope {
    private val partNames = mutableSetOf<String>()
    private val parts = mutableListOf<Part>()

    private val dayPadded = day.toString().padStart(2, '0')

    private fun add(part: Part) {
        if (partNames.contains(part.name)) {
            throw IllegalStateException("Only one part block with name ${part.name} is allowed.")
        }
        partNames.add(part.name)
        parts.add(part)
    }

    override fun part(name: String, block: () -> Any?) = add(Part.Unscoped(name, block))
    override fun <T> part(name: String, scope: T, block: T.() -> Any?) = add(Part.Scoped(name, scope, block))

    override val defaultInputFile: File
        get() = File("src/main/kotlin/fyi/mayr/adventofcode/y$year/d$dayPadded/input.txt")

    fun execute() {
        parts.forEach {
            println("Y$year - Day $dayPadded - Part ${it.name}: ${it.runBlock().toString()}")
        }
    }
}

sealed class Part(val name: String) {
    abstract fun runBlock() : Any?
    class Unscoped(name: String, private val block: () -> Any?) : Part(name) {
        override fun runBlock(): Any? = block()
    }

    class Scoped<T>(name: String, private val scope: T, private val block: T.() -> Any?) : Part(name) {
        override fun runBlock(): Any? = block(scope)
    }
}

@AdventOfCodeDsl
fun adventOfCode(year: Int, day: Int, block: AdventOfCodeScope.() -> Unit): Runnable {
    val scope = AdventOfCodeScopeImpl(year, day)
    block(scope)
    return Runnable{ scope.execute() }
}

