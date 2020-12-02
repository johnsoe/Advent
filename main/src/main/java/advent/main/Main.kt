package advent.main

import advent.tasks.twenty.DayOne

fun main(args: Array<String>) {
    val dayOne = DayOne()

    println(dayOne.findAccountingError())
    println(dayOne.findThirdAccountingError())
}
