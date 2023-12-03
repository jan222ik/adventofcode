package fyi.mayr.adventofcode.y2023.d01

import fyi.mayr.adventofcode.util.adventOfCode

fun main() = adventOfCode(2023, 1) {
    val lines = defaultInputFile.readLines()

    part2("V3", scope = Part2V3Regex) {
        lines.sumOf {
            firstMatchDigit(it, DIGITS_REGEX) * 10 + firstMatchDigit(it, LAST_DIGIT_REGEX)
        }
    }
}

private object Part2V3Regex {
    const val DIGITS_REGEX = "(one|1)|(two|2)|(three|3)|(four|4)|(five|5)|(six|6)|(seven|7)|(eight|8)|(nine|9)"

    /*
       Last Digit Pattern Explanation:
       (?:.*) : Non-capturing group of greedy capture
       (?:$REGEX)+ : Non-capturing group of normal pattern with at least one occurrence, the latest matched REGEX capture group will be returned in find.
       (?:.*?) : Non-capturing group of reluctant word end to force greedy capture to expand
    */
    const val LAST_DIGIT_REGEX = "(?:.*)(?:$DIGITS_REGEX)+(?:.*?)"

    /**
     * Gets the first matching digit by getting the index of the matching capture group, skipping the first capture group.
     */
    fun firstMatchDigit(input: String, regexStr: String): Int {
        return regexStr.toRegex().find(input)?.groups?.indexOfLast { it != null } ?: error("unreachable")
        // use indexOfLast because group[0] is the whole match, therefore going from the back to the front.
    }
}

