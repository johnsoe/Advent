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

        private fun directionHelper(cur: Direction, offset: Int): Direction {
            return directions[(directions.indexOf(cur) + offset) % directions.size]
        }

        fun getNextPoint(cur: Point, dir: Direction): Point {
            return when (dir) {
                Direction.North -> Point(cur.x, cur.y + 1)
                Direction.South -> Point(cur.x, cur.y - 1)
                Direction.East -> Point(cur.x + 1, cur.y)
                Direction.West -> Point(cur.x - 1, cur.y)
            }
        }
    }
}