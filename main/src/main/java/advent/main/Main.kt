package advent.main

import advent.tasks.DayOne

fun main(args: Array<String>) {
    val dayOne = DayOne()
    println(dayOne.calcNeededFuel())
    println(dayOne.calcIterFuel())
}
