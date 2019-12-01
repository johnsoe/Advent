package advent.tasks

import advent.helper.Day

class DayOne : Day() {

    override fun getFileName() = "one_a.txt"

    fun calcNeededFuel(): Long {
        return getInputByLine().map {
            it.toLong() / 3L - 2L
        }.sum()
    }

    fun calcIterFuel(): Long {
        return getInputByLine().map {
            var sum = 0L
            var temp = it.toLong() / 3L - 2L
            while (temp > 0) {
                sum += temp
                temp = temp / 3L - 2L
            }
            sum
        }.sum()
    }
}