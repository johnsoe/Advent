package advent.tasks

import advent.helper.Day

class DayOne : Day() {

    override fun getFileName() = "one_a.txt"

    fun calcNeededFuel(): Long {
        return getInputByLine().map { calcFuel(it.toLong()) }.sum()
    }

    fun calcFuel(mass: Long): Long = mass / 3 - 2

    fun calcIterFuel(): Long {
        return getInputByLine().map { calcIterFuel(it.toLong()) }.sum()
    }

    private fun calcIterFuel(mass: Long) : Long {
        val fuel = calcFuel(mass)
        return if (fuel <= 0) {
            0
        } else {
            fuel + calcIterFuel(fuel)
        }
    }
}