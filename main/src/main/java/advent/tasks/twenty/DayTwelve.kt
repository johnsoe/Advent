package advent.tasks.twenty

import advent.helper.Day
import advent.helper.Util
import advent.helper.Util.*
import advent.helper.Util.Companion.translateByDirection
import java.awt.Point
import kotlin.math.abs

class DayTwelve: Day() {
    override fun getFileName() = "twenty/twelve.txt"

    /**
     *   1 2
     *   2 -1
     *   -1 -2
     *   -2 1
     *   1 2
     */

    fun getManhattanDistance(): Int {
        var facing: Direction = Direction.East
        val position = Point(0, 0)
        getInputByLine().forEach {
            val instruction = it[0]
            val amount = it.substring(1).toInt()
            when(instruction) {
                'N' -> position.translate(0, amount)
                'S' -> position.translate(0, -amount)
                'E' -> position.translate(amount, 0)
                'W' -> position.translate(-amount, 0)
                'F' -> position.translateByDirection(facing, amount)
                'R' -> facing = Util.getClockwiseDirection(facing, amount / 90)
                'L' -> facing = Util.getCounterClockwiseDirection(facing, amount / 90)
            }
        }
        return position.manhattanDistance()
    }

    fun getManhattanDistanceWithWaypoint(): Int {
        var wp = Point(10, 1)
        val position = Point(0, 0)
        getInputByLine().forEach {
            val instruction = it[0]
            val amount = it.substring(1).toInt()
            when(instruction) {
                'N' -> wp.translate(0, amount)
                'S' -> wp.translate(0, -amount)
                'E' -> wp.translate(amount, 0)
                'W' -> wp.translate(-amount, 0)
                'F' -> position.translate(wp.x * amount, wp.y * amount)
                else -> {
                    wp = rotatePoint(amount, instruction, wp)
                }
            }
        }
        return position.manhattanDistance()
    }

    private fun rotatePoint(rotation: Int, dir: Char, start: Point): Point {
        return when {
            rotation == 180 -> Point(-start.x, -start.y)
            (rotation == 90 && dir == 'R') || (rotation == 270 && dir == 'L') -> Point(start.y, -start.x)
            (rotation == 90 && dir == 'L') || (rotation == 270 && dir == 'R') -> Point(-start.y, start.x)
            else -> {
                println("Invalid input: $rotation $dir")
                Point(0,0)
            }
        }
    }

    private fun Point.manhattanDistance(): Int {
        return abs(this.x) + abs(this.y)
    }
}