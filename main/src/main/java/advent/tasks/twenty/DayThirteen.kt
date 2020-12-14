package advent.tasks.twenty

import advent.helper.Day
import kotlin.math.min

class DayThirteen: Day() {
    override fun getFileName() = "twenty/thirteen.txt"

    fun getSoonestBusId(): Long {
        val startTime = getInputByLine().first().toLong()
        return getInputByLine().last().split(",")
            .filter { it != "x" }
            .map { it.toLong() }
            .map { it to (it - startTime % it) }
            .minBy { it.second }?.let {
                it.first * it.second
            } ?: -1

    }

    fun consecutiveBusIdTimestamps(): Long {
        val busTimes = getInputByLine().last().split(",")
            .withIndex()
            .filter { it.value != "x" }
            .map { it.value.toLong() to it.index.toLong() }

        var curIndex = 0L
        var factor = busTimes.first().first
        var subListCount = 1
        while(true) {
            val subList = busTimes.subList(0, subListCount + 1)
            if (subList.all { (curIndex + it.second) % it.first == 0L }) {
                factor *= subList.last().first
                subListCount++
                if (subListCount == busTimes.size) {
                    return curIndex
                }
            }
            curIndex += factor
        }
        return 0L
    }
}