package fyi.mayr.adventofcode.y2023.d01

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 1) {
    val lines = defaultInputFile.readLines()
    part2("V1", Part2V1) {
        lines.sumOf { extractNumber(it) }
    }
}.run()

private object Part2V1 {

    private val numberNames = mapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9
    )

    fun extractNumber(input: String): Int {
        val idxByValue = mutableMapOf<Int, Int>()
        val addToMap: (Int) -> ((Int) -> Unit) = { value -> { idx -> idxByValue[idx] = value } }
        numberNames.forEach { (key, value) ->
            input.indexOf(key).exists()?.let(addToMap(value))
            input.lastIndexOf(key).exists()?.let(addToMap(value))
        }
        input.indexOfFirst(Char::isDigit).exists()?.let { idx -> addToMap(input[idx].digitToInt())(idx) }
        input.indexOfLast(Char::isDigit).exists()?.let { idx -> addToMap(input[idx].digitToInt())(idx) }

        return idxByValue
            .toList()
            .sortedBy { it.first }
            .let { it.first().second.times(10).plus(it.last().second) }
    }

    private fun Int.exists(): Int? = this.takeIf { it > -1 }
}
