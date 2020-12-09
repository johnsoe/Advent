package advent.tasks.twenty

import advent.helper.Day

class DayNine: Day() {
    override fun getFileName() = "twenty/nine.txt"

    companion object {
        private const val WINDOW_LENGTH = 25
    }

    fun findFirstInvalidInput(): Long {
        val input = getInputByLine().map { it.toLong() }
        return input.takeLast(input.size - WINDOW_LENGTH)
            .mapIndexed { index, s ->
                val preamble = input.subList(index, index + WINDOW_LENGTH).toSet()
                Util.getSummedPair(preamble, s)?.let {
                    0L
                } ?: s
            }.first { it != 0L }
    }

    fun findContiguousSum(): Long {
        val input = getInputByLine().map { it.toLong() }
        val target = findFirstInvalidInput()
        var frontIndex = 0
        var backIndex = 0
        var sum = 0L
        while (sum != target) {
            if (sum < target) {
                sum += input[frontIndex]
                frontIndex++
            } else {
                sum -= input[backIndex]
                backIndex++
            }
        }
        return input.subList(backIndex, frontIndex).let {
            (it.min() ?: 0) + (it.max() ?: 0)
        }
    }
}