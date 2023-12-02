package fyi.mayr.adventofcode.y2023.d01

import java.io.File

fun main(args: Array<String>) {
    val lines = File("src/main/kotlin/fyi/mayr/adventofcode/y2023/d01/input.txt").readLines()
    println("Part 2V3: sum = ${part2(lines)}")
}

private const val REGEX = "(one|1)|(two|2)|(three|3)|(four|4)|(five|5)|(six|6)|(seven|7)|(eight|8)|(nine|9)"

private fun part2(lines: List<String>) = lines.sumOf {
    "${firstMatchDigit(it, REGEX)}${firstMatchDigit(it, "(?:.*)(?:$REGEX)+(?:.*?)")}".toInt()
}

/*
   Match Last Digit Pattern Explanation:
   (?:.*) : Non-capturing group of greedy capture
   (?:$REGEX)+ : Non-capturing group of normal pattern with at least one occurrence, the latest matched REGEX capture group will be returned in find.
   (?:.*?) : Non-capturing group of reluctant word end to force greedy capture to expand
*/

private fun firstMatchDigit(input: String, regexStr: String): Int {
    return regexStr.toRegex().find(input)?.groups?.indexOfLast { it != null } ?: error("unreachable")
}

