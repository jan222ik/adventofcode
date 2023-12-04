package fyi.mayr.adventofcode.y2023.d04

import fyi.mayr.adventofcode.util.adventOfCode
import kotlin.math.pow


fun main() = adventOfCode(2023, 4) {
    val lines = defaultInputFile.readLines()
    val cards = parse(lines)
    part1 {
        cards.sumOf { card ->
            val size = card.winCount
            size.takeUnless { it > 0 } ?: 2.0.pow(size.dec()).toInt()
        }
    }

    part2("V1") {
        val cardCountForSeenCards = mutableMapOf<Int, Int>()
        cards.reversed().fold(0) { cardCount: Int, card: Card ->
            val winners = card.winCount
            val cardsFromWins = 1 + (0..winners).sumOf { cardCountForSeenCards[card.cardId + it] ?: 0 }
            cardCountForSeenCards.putIfAbsent(card.cardId, cardsFromWins)
            cardCount + cardsFromWins
        }
    }

    part2("V2") {
        cards.reversed()
            .map(Card::winCount)
            .fold(emptyList<Int>()) { acc, winCount ->
                listOf(1 + (0..<winCount).sumOf { acc[it] }) + acc
            }.sum()
    }
}

private data class Card(val cardId: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
    val winCount
        get() = determineWinners().size

    private fun determineWinners(): List<Int> {
        val winningNumberSet = winningNumbers.toSet()
        return numbers.filter { winningNumberSet.contains(it) }
    }
}

private fun parse(lines: List<String>): List<Card> = buildList {
    for (line in lines) {
        val (cardId, winning, actual) = line.split(":", "|")
        add(Card(
            cardId = cardId.split(" ").last().toInt(),
            winningNumbers = winning.split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
            numbers = actual.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        ))
    }
}
