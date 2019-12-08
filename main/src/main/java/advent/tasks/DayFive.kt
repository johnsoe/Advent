package advent.tasks

import advent.helper.Day
import advent.helper.IntCodeComputer

class DayFive : Day() {

    override fun getFileName() = "five.txt"

    fun parseInstructions() {
        val comp = IntCodeComputer(getInputBySeparator().map { it.toInt() }.toMutableList())
        println(comp.parseAllInstructions(listOf(5)))
        println(comp.instructions)
    }
}