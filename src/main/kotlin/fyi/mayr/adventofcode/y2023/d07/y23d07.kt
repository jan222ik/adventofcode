package fyi.mayr.adventofcode.y2023.d07

import fyi.mayr.adventofcode.util.adventOfCode
import fyi.mayr.adventofcode.y2023.d07.CardValue.A
import fyi.mayr.adventofcode.y2023.d07.CardValue.C2
import fyi.mayr.adventofcode.y2023.d07.CardValue.C3
import fyi.mayr.adventofcode.y2023.d07.CardValue.C4
import fyi.mayr.adventofcode.y2023.d07.CardValue.C5
import fyi.mayr.adventofcode.y2023.d07.CardValue.C6
import fyi.mayr.adventofcode.y2023.d07.CardValue.C7
import fyi.mayr.adventofcode.y2023.d07.CardValue.C8
import fyi.mayr.adventofcode.y2023.d07.CardValue.C9
import fyi.mayr.adventofcode.y2023.d07.CardValue.Companion
import fyi.mayr.adventofcode.y2023.d07.CardValue.J
import fyi.mayr.adventofcode.y2023.d07.CardValue.K
import fyi.mayr.adventofcode.y2023.d07.CardValue.Q
import fyi.mayr.adventofcode.y2023.d07.CardValue.T
import org.testng.annotations.Test

fun main() = adventOfCode(2023, 7) {
    val lines = defaultInputFile.readLines()

    val hands = lines.map { it.split(" ") }
        .map { Hand(it.first().map(Companion::from), it.last().toInt()) }
    part1 {
        sortedHands(hands, Hand::getScore, valuePart1)
            .toList()
            .reversed()
            .foldIndexed(0) { index, acc, hand ->
                acc + index.inc().times(hand.bid)
            }
    }

    part2 {
        sortedHands(hands, Hand::getScoreWithJokers, valuePart2)
            .toList()
            .reversed()
            .foldIndexed(0) { index, acc, hand ->
                acc + index.inc().times(hand.bid)
            }
    }
}

private fun sortedHands(hands: List<Hand>, transform: (Hand) -> Int, valueProgression: List<CardValue>) =
    hands.groupBy(transform)
        .asSequence()
        .sortedByDescending { it.key }
        .map { handsByKey ->
            handsByKey.value.sortedByDescending { hand ->
                hand.hand.joinToString("") {
                    valueProgression.indexOf(it).plus(65).toChar().toString()
                }
            }
        }
        .flatten()

private data class Hand(val hand: List<CardValue>, val bid: Int) {
    val hist = hand.groupBy { it }
        .entries
        .sortedByDescending { it.value.size }
}

private fun Hand.getScore(): Int {
    val histogram = hist.take(2)
    return histogram.first().value.size * 1000000 + (histogram.getOrNull(1)?.value?.size?.times(100) ?: 0)
}

private fun Hand.getScoreWithJokers(): Int {
    val histogram = hist.toMutableList()
    val jokers = histogram.indexOfFirst { it.key == J }.takeUnless { it == -1 }
        ?.let { idx -> histogram.removeAt(idx).value.size } ?: 0
    return (histogram.getOrNull(0)?.value?.size?.plus(jokers)
        ?: jokers) * 1000000 + (histogram.getOrNull(1)?.value?.size?.times(100) ?: 0)
}

private val valuePart1 = listOf(A, K, Q, J, T, C9, C8, C7, C6, C5, C4, C3, C2).reversed()

private val valuePart2 = listOf(A, K, Q, T, C9, C8, C7, C6, C5, C4, C3, C2, J).reversed()
private enum class CardValue() {
    A, K, Q, J, T, C9, C8, C7, C6, C5, C4, C3, C2;
    companion object {
        fun from(char: Char): CardValue {
            val search = if (char.isDigit()) "C$char" else "$char"
            return CardValue.valueOf(search)
        }
    }

}
@Suppress("ClassName")
class y23d07p1Test {

    @Test
    fun test1() {
        val sortedHands = sortedHands(
            hands = CardValue.entries.map { v -> Hand(List(5) { v }, 123) },
            transform = Hand::getScore,
            valueProgression = valuePart1
        ).toList()
        println("sortedHands = $sortedHands")
    }

    @Test
    fun test2() {
        val sortedHands = sortedHands(
            hands = listOf(
                Hand(listOf(A, A, A, A, A), 1),
                Hand(listOf(A, K, K, K, K), 1),
                Hand(listOf(A, K, K, K, C2), 1),
                Hand(listOf(A, K, K, K, C3), 1),
            ), Hand::getScore, valuePart1
        ).toList()
        println("sortedHands = $sortedHands")
    }

    @Test
    fun fullHouse() {
        val sortedHands = sortedHands(
            hands = listOf(
                Hand(listOf(A, A, A, A, A), 0),
                Hand(listOf(K, K, K, K, A), 1),
                Hand(listOf(A, A, K, K, K), 3),
                Hand(listOf(K, K, K, A, A), 4),
                Hand(listOf(A, A, A, C2, C2), 2)
            ), Hand::getScore, valuePart1
        ).toList()
        println("sortedHands = $sortedHands")
        sortedHands.forEachIndexed { index, hand -> assert(index == hand.bid) }
    }

    @Test
    fun test3() {
        val sortedHands = sortedHands(
            hands = listOf(
                Hand(listOf(A, A, A, A, A), 0),
                Hand(listOf(A, A, A, A, K), 1),
                Hand(listOf(A, A, A, K, K), 2),
                Hand(listOf(K, K, K, A, A), 3),
                Hand(listOf(K, K, K, A, C3), 4),
                Hand(listOf(A, A, K, C3, C3), 5),
                Hand(listOf(A, A, K, C2, C3), 6),
                Hand(listOf(A, K, C2, C3, C4), 7),
                Hand(listOf(A, K, C2, C3, C5), 8),
            ), Hand::getScore, valuePart1
        ).toList()
        println("sortedHands = $sortedHands")
    }

    @Test
    fun test4() {
        /*
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
         */
        val sortedHands = sortedHands(
            hands = listOf(
                Hand(listOf(C3, C2, T, C3, K), 765),
                Hand(listOf(T, C5, C5, J, C5), 684),
                Hand(listOf(K, K, C6, C7, C7), 28),
                Hand(listOf(K, T, J, J, T), 220),
                Hand(listOf(Q, Q, Q, J, A), 483)
            ), Hand::getScore, valuePart1
        ).toList()
        println("sortedHands = $sortedHands")
        sortedHands.reversed().foldIndexed(0) { index, acc, hand ->
            acc + index.inc().times(hand.bid)
        }.also { println(it) }
    }
}
