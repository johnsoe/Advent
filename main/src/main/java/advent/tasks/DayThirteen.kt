package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer
import java.awt.Point
import java.lang.IllegalStateException

class DayThirteen: Day() {
    override fun getFileName() = "thirteen.txt"

    fun countBlockTiles(): Int{
        val input = getInputBySeparator(",").map { it.toLong() }.toMutableList()
        val outputs = LongCodeComputer(input).parseAllInstructions(listOf()).map { it.toInt() }

        val state = mutableMapOf<Point, Tile>()
        for (i in 0..outputs.size step 3) {
            if (i + 2 < outputs.size) {
                val point = Point(outputs[i], outputs[i+1])
                state[point] = getTile(outputs[i+2])
            }
        }
        return state.count {
            it.value == Tile.Block
        }
    }

    private fun getTile(id: Int): Tile {
        return when(id) {
            0 -> Tile.Empty
            1 -> Tile.Wall
            2 -> Tile.Block
            3 -> Tile.Paddle
            4 -> Tile.Ball
            else -> throw IllegalStateException("Weird state: $id")
        }
    }

    sealed class Tile {
        object Empty: Tile()
        object Wall: Tile()
        object Block: Tile()
        object Paddle: Tile()
        object Ball: Tile()
    }
}