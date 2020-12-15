package advent.tasks.twenty

import advent.helper.Day

class DayFifteen: Day() {
    override fun getFileName(): String {
        return "twenty/fifteen.txt"
    }

    fun getNthDigitFromGameSequence(n: Int): Int {
        val input = getInputBySeparator().map { it.toInt() }
        var prev = input.last()
        val map = generateStartMap(input)
        for (i in input.size until n) {
            val indices = map[prev]!!
            prev = if (indices.size == 1) {
                0
            } else {
                indices.takeLast(2).reduce { acc, l -> l - acc }
            }
            map.putIfAbsent(prev, mutableListOf())
            map[prev]!!.add(i)
        }
        return prev
    }

    private fun generateStartMap(input: List<Int>): MutableMap<Int, MutableList<Int>> {
        return mutableMapOf<Int, MutableList<Int>>().apply {
            input.forEachIndexed { index, s ->
                this[s] = mutableListOf(index)
            }
        }
    }
}