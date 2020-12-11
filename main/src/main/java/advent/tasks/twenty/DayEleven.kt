package advent.tasks.twenty

import advent.helper.Day
import java.util.*
import kotlin.math.min
import kotlin.math.max

class DayEleven: Day() {
    override fun getFileName() = "twenty/eleven.txt"

    fun getStableCount(useVisibilitySeating: Boolean): Int {
        var seats = getInputByLine()
        var changed = true
        while (changed) {
            val nextState = mutableListOf<String>()
            seats.forEachIndexed { rowIndex, row ->
                val newRow = row.mapIndexed { colIndex, _ ->
                    getNextState(rowIndex, colIndex, seats, useVisibilitySeating)
                }.joinToString("")
                nextState.add(newRow)
            }
            changed = seats.withIndex().any {
                it.value != nextState[it.index]
            }
            seats = nextState
        }
        return seats.map { row ->
            row.count { it == '#' }
        }.sum()
    }

    private fun getNextState(
        row: Int,
        col: Int,
        seats: List<String>,
        useVisibilitySeating: Boolean
    ): Char {
        val seat = seats[row][col]
        return if (seat == '.') {
            seat
        } else {
            val neighborCount = if(useVisibilitySeating) {
                getOccupiedVisibleCount(row, col, seats)
            } else {
                getOccupiedNeighborCount(row, col, seats)
            }
            when {
                seat == 'L' && neighborCount == 0 -> '#'
                seat == '#' && neighborCount >= 4 && !useVisibilitySeating -> 'L'
                seat == '#' && neighborCount >= 5 -> 'L'
                else -> seat
            }
        }
    }

    private fun rowRange(row: Int, seats: List<String>): IntRange = max(row - 1, 0)..min(row + 1, seats.size-1)
    private fun colRange(col: Int, seats: List<String>) = max(col - 1, 0)..min(col + 1, seats[0].length-1)

    private fun getOccupiedNeighborCount(row: Int, col: Int, seats: List<String>): Int {
        val neighbors = mutableListOf<Char>()
        rowRange(row, seats).forEach { rc ->
            colRange(col, seats).forEach { cc ->
                if (rc != row || cc != col) {
                    neighbors.add(seats[rc][cc])
                }
            }
        }
        return neighbors.count {
            it == '#'
        }
    }

    private fun getOccupiedVisibleCount(row: Int, col: Int, seats: List<String>): Int {
        return mutableListOf(
            getNextVisibleSeat(row, col, seats, 0 to 1),
            getNextVisibleSeat(row, col, seats, 0 to -1),
            getNextVisibleSeat(row, col, seats, 1 to 0),
            getNextVisibleSeat(row, col, seats, 1 to 1),
            getNextVisibleSeat(row, col, seats, 1 to -1),
            getNextVisibleSeat(row, col, seats, -1 to 0),
            getNextVisibleSeat(row, col, seats, -1 to 1),
            getNextVisibleSeat(row, col, seats, -1 to -1)
        ).count { it == '#' }
    }

    private fun getNextVisibleSeat(row: Int, col: Int, seats: List<String>, slope: Pair<Int, Int>): Char {
        var nextR = row + slope.first
        var nextC = col + slope.second
        while (nextR >= 0 && nextR < seats.size && nextC >= 0 && nextC < seats[0].length) {
            seats[nextR][nextC].let {
                if (it != '.') return it
            }
            nextR += slope.first
            nextC += slope.second
        }
        return '.'
    }
}