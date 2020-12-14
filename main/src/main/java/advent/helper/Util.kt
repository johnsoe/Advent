package advent.helper

import java.awt.Point

class Util {

    sealed class Direction(val d: Int) {
        object North: Direction(1)
        object South: Direction(2)
        object West: Direction(3)
        object East: Direction(4)
    }

    companion object {
        val pointCache = mutableSetOf<Point>()
        val directions = listOf(Direction.North, Direction.East, Direction.South, Direction.West)

        fun getNextDirection(cur: Direction) = directionHelper(cur, 1)
        fun getOppositeDirection(cur: Direction) = directionHelper(cur, 2)
        fun getClockwiseDirection(cur: Direction, rotation: Int) = directionHelper(cur, rotation)
        fun getCounterClockwiseDirection(cur: Direction, rotation: Int) = directionHelper(cur, (-rotation % directions.size + directions.size))

        private fun directionHelper(cur: Direction, offset: Int): Direction {
            return directions[(directions.indexOf(cur) + offset) % directions.size]
        }

        fun Point.translateByDirection(dir: Direction, amount: Int = 1) {
            when (dir) {
                Direction.North -> this.translate(0, amount)
                Direction.South -> this.translate(0, -amount)
                Direction.East -> this.translate(amount, 0)
                Direction.West -> this.translate(-amount, 0)
            }
        }

        fun getNextPoint(cur: Point, dir: Direction, offset: Int = 1): Point {
            return when (dir) {
                Direction.North -> Point(cur.x, cur.y + offset)
                Direction.South -> Point(cur.x, cur.y - offset)
                Direction.East -> Point(cur.x + offset, cur.y)
                Direction.West -> Point(cur.x - offset, cur.y)
            }
        }

        fun toAscii(input: String): List<Long> {
            return input.map { it.toLong() }.toMutableList().apply {
                add(10L)
            }
        }

        fun fromAscii(input: List<Long>): String {
            val str = input.map { it.toChar() }.toString()
            return str.replace(", ", "")
        }

        fun getSummedPair(nums: Set<Long>, target: Long): Pair<Long, Long>? {
            return nums.firstOrNull { nums.contains(target - it) }?.let {
                it to (target - it)
            }
        }
    }
}