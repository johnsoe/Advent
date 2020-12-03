package advent.main

import advent.tasks.twenty.DayThree
import advent.tasks.twenty.DayTwo

fun main(args: Array<String>) {
//    val dayOne = DayOne()
//    println(dayOne.findAccountingError())
//    println(dayOne.findThirdAccountingError())

//    val dayTwo = DayTwo()
//    println(dayTwo.getOldValidPasswordCount())
//    println(dayTwo.getNewValidPasswordCount())

    val dayThree = DayThree()
    println(dayThree.getTreeEncounterCount(3))
    println(dayThree.getMultipleForSlopes())
}
