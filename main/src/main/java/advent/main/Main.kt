package advent.main

import advent.tasks.DayFour
import advent.tasks.DayOne
import advent.tasks.DayThree
import advent.tasks.DayTwo

fun main(args: Array<String>) {
    val dayOne = DayOne()
    println(dayOne.calcNeededFuel())
    println(dayOne.calcIterFuel())

//    val dayTwo = DayTwo()
//    println(dayTwo.calcGravityAssist(12, 2))
//    println(dayTwo.calcSpecificOutput(19690720))

//    val dayThree = DayThree()
//    dayThree.findLowestDistance()

    println(DayFour().findValidPassword(168630..718098))
    println(DayFour().findStrictPassword(168630..718098))
}
