package advent.tasks.twenty

import advent.helper.Day
import advent.helper.Util

class DayOne : Day() {

    override fun getFileName(): String {
        return "twenty/one.txt"
    }

    fun findAccountingError(): Long {
        val set = getInputByLine().map { it.toLong() }.toSet()
        return Util.getSummedPair(set, 2020L)?.multiply() ?: 0L
    }

    private fun Pair<Long, Long>.multiply(): Long {
        return first * second
    }

    fun findThirdAccountingError(): Long {
        val set = getInputByLine().map { it.toLong() }.toSet()
        set.forEach { outer ->
            val target = 2020 - outer
            Util.getSummedPair(set, target)?.let {
                return it.multiply() * outer
            }
        }
        return 0
    }
}