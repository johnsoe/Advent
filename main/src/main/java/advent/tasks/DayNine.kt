package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer

class DayNine: Day() {
    override fun getFileName()= "nine.txt"

    fun checkComputer() {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
//        println(comp.parseAllInstructions(listOf(1)))
        println(comp.parseAllInstructions(listOf(2)))
    }
}