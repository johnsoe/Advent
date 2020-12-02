package advent.tasks.twenty

import advent.helper.Day

class DayOne : Day() {

    override fun getFileName(): String {
        return "twenty/one.txt"
    }

    fun findAccountingError(): Int {
        val set = getInputByLine().map { it.toInt() }.toSet()
        return accountingHelper(set, 2020)?.multiply() ?: 0
    }

    private fun accountingHelper(nums: Set<Int>, target: Int): Pair<Int, Int>? {
        return nums.firstOrNull { nums.contains(target - it) }?.let {
            it to (target - it)
        }
    }

    private fun Pair<Int, Int>.multiply(): Int {
        return first * second
    }

    fun findThirdAccountingError(): Int {
        val set = getInputByLine().map { it.toInt() }.toSet()
        set.forEach { outer ->
            val target = 2020 - outer
            accountingHelper(set, target)?.let {
                return it.multiply() * outer
            }
        }
        return 0
    }
}