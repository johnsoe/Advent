package advent.tasks

import advent.helper.Day
import kotlin.math.abs

class DayTwentyTwo : Day() {
    override fun getFileName(): String {
        return "twenty_two.txt"
    }

    fun shuffleDeck(deckSize: Int): Int {
        val deck = internalShuffle(deckSize.toLong())
        return deck.indexOf(2019)
    }

    private fun internalShuffle(size: Long): MutableList<Long> {
        val deck = mutableListOf<Long>()
        for (i in 0L until size) { deck.add(i) }
        getInputByLine().map { getShuffleType(it) }.forEach {
//            println(it)
            when (it) {
                is ShuffleType.Cut -> cut(it.place, deck)
                is ShuffleType.NewStack -> newStack(deck)
                is ShuffleType.Increment -> incShuffle(it.inc, deck)
            }
//            printDeck(deck)
        }
        return deck
    }

//    fun excessiveShuffle(size: Long, repitions: Long): Long {
//        var deck= internalShuffle(size).toList()
//        val shift = LongArray(deck.size)
//        deck.forEachIndexed { index, l ->
//            shift[l.toInt()] = index - l
//        }
//        for (i in 1 until repitions) {
//            deck = deck.mapIndexed { index, l ->
//                deck[index + shift[index]]
//            }
//        }
//    }

    private fun printDeck(deck: MutableList<Long>) {
        deck.forEachIndexed { _, l -> print("$l ") }
        println()
    }

    fun test() {
        var deck = mutableListOf<Long>(0,1,2,3,4,5,6,7,8,9)
        cut(3, deck)
        printDeck(deck)

        deck = mutableListOf(0,1,2,3,4,5,6,7,8,9)
        cut(-4, deck)
        printDeck(deck)

        deck = mutableListOf(0,1,2,3,4,5,6,7,8,9)
        newStack(deck)
        printDeck(deck)

        deck = mutableListOf(0,1,2,3,4,5,6,7,8,9)
        incShuffle(3, deck)
        printDeck(deck)
    }

    private fun getShuffleType(input: String): ShuffleType {
        val words = input.split(" ")
        return when {
            words[0] == "cut" -> ShuffleType.Cut(words[1].toInt())
            words[3] == "stack" -> ShuffleType.NewStack
            else -> ShuffleType.Increment(words[3].toInt())
        }
    }

    private fun cut(place: Int, deck: MutableList<Long>) {
        val p = if (place > 0) place else deck.size - abs(place)
        val map = deck.mapIndexed { index, _ ->
            deck[(index + p) % deck.size]
        }
        deck.forEachIndexed { index, _ -> deck[index] = map[index]}
    }

    private fun newStack(deck: MutableList<Long>) {
        deck.reverse()
    }

    private fun incShuffle(inc: Int, deck: MutableList<Long>) {
        val temp = mutableListOf<Long>().apply { addAll(deck) }
        temp.forEachIndexed { index, i ->
            deck[(index * inc) % deck.size] = i
        }
    }

    sealed class ShuffleType {
        data class Cut(val place: Int): ShuffleType()
        data class Increment(val inc: Int): ShuffleType()
        object NewStack: ShuffleType()
    }
}