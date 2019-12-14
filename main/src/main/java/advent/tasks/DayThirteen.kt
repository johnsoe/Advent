package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer
import java.awt.Point
import java.lang.IllegalStateException

class DayThirteen: Day() {
    override fun getFileName() = "thirteen.txt"

    fun countBlockTiles(): Int{
        val input = getInputBySeparator().map { it.toLong() }.toMutableList()
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

    fun playBrickBreaker(): Int {
        val input = getInputBySeparator().map { it.toLong() }.toMutableList()
        input[0] = 2L
        val comp = LongCodeComputer(input)

        var state = GameState()
        var opCount = 0
        while (comp.lastOpcode != LongCodeComputer.Opcode.Terminate) {
            val outputs = comp.parseAllInstructions(listOf(state.joystick.toLong()), 3).map { it.toInt() }
            if (outputs.size != 3) break
            if (outputs[0] == -1 && outputs[1] == 0) {
                state.score = outputs[2]
            } else {
                val tile = getTile(outputs[2])
                val point = Point(outputs[0], outputs[1])
                state.board[point] = tile

                when (tile) {
                    Tile.Paddle -> state.paddlePosition = point
                    Tile.Ball -> state.ballPosition = point
                }
                updateJoystick(state)
            }
            opCount++
        }
        return state.score
    }

    private fun updateJoystick(state: GameState) {
        when {
            state.ballPosition.x < state.paddlePosition.x -> state.joystick = -1
            state.ballPosition.x > state.paddlePosition.x -> state.joystick = 1
            else -> state.joystick = 0
        }
    }

    private class GameState {
        var score = 0
        var paddlePosition = Point(0, 0)
        var ballPosition = Point(0, 0)
        var joystick = 0
        var board = mutableMapOf<Point, Tile>()
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
        object Empty : Tile()
        object Wall : Tile()
        object Block : Tile()
        object Paddle : Tile()
        object Ball : Tile()
    }
}
