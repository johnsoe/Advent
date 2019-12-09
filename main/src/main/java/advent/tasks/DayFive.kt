package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer

class DayFive : Day() {

    override fun getFileName() = "five.txt"

    fun parseInstructions() {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
        println(comp.parseAllInstructions(listOf(5)))
    }
}