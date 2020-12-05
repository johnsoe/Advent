package advent.tasks.twenty

import advent.helper.Day

class DayFive: Day() {
    override fun getFileName() = "twenty/five.txt"

    fun getMaxSeatId(): Int {
        return getSeatIds().max() ?: 0
    }

    private fun getSeatIds(): Set<Int> {
        return getInputByLine().map { encodedSeat ->
            val binaryStr = encodedSeat.map {
                when(it) {
                    'F' -> '0'
                    'B' -> '1'
                    'R' -> '1'
                    'L' -> '0'
                    else -> {
                        println("Invalid character: $it")
                    }
                }
            }.joinToString("")
            val row = binaryStr.substring(0..6).toInt(2)
            val col = binaryStr.substring(7).toInt(2)
            row * 8 + col
        }.toSet()
    }

    fun getMySeatId(): Int {
        return getSeatIds().let {
            it.firstOrNull { id ->
                !it.contains(id + 1) && it.contains(id + 2)
            }
        }?.plus(1) ?: 0
    }
}