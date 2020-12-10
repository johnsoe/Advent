package advent.tasks.twenty

import advent.helper.Day

class DayTen: Day() {
    override fun getFileName() = "twenty/ten.txt"

    fun getJoltageDifference(): Int {
        val sortedJoltage = getSortedJoltages()
        val mapped = sortedJoltage.mapIndexed { index, joltage ->
            if (index < sortedJoltage.size - 1) {
                sortedJoltage[index + 1] - joltage
            } else {
                3
            }
        }
        return mapped.count { it == 3 } * mapped.count { it == 1 }
    }

    private fun getSortedJoltages(): List<Int> {
        return getInputByLine()
            .map { it.toInt() }
            .toMutableList()
            .apply { this.add(0) }
            .sorted()
    }

    fun getJoltagePermutations(): Long {
        return helper(0, getSortedJoltages(), mutableMapOf())
    }

    private fun helper(cur: Int, sorted: List<Int>, pathMap: MutableMap<Int, Long>): Long {
        return if (pathMap.containsKey(cur)) {
            pathMap[cur]!!
        } else {
            val nextOptions = sorted.filter { it in (cur + 1)..(cur + 3) }
            if (nextOptions.isNotEmpty()) {
                val subPathCount = nextOptions.map {
                    helper(it, sorted, pathMap)
                }.sum()
                pathMap[cur] = subPathCount
                subPathCount
            } else {
                1
            }
        }
    }
}