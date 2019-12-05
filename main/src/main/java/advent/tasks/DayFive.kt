package advent.tasks

import advent.helper.Day
import advent.helper.IntCodeComputer

class DayFive : Day() {

    override fun getFileName() = "five.txt"

    fun parseInstructions() {
        val comp = IntCodeComputer(getInputBySeparator().map { it.toInt() }.toMutableList(), 5)
        println(comp.parseAllInstructions())
        println(comp.instructions)
    }
}