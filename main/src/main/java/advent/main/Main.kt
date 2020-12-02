package advent.main

import advent.tasks.twenty.DayTwo

fun main(args: Array<String>) {
//    val dayOne = DayOne()
//
//    println(dayOne.findAccountingError())
//    println(dayOne.findThirdAccountingError())

    val dayTwo = DayTwo()
    println(dayTwo.getOldValidPasswordCount())
    println(dayTwo.getNewValidPasswordCount())
}
