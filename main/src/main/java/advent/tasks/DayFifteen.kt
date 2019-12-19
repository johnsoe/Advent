import advent.helper.Day
import advent.helper.LongCodeComputer
import java.awt.Point
import java.lang.Integer.max

class DayFifteen : Day() {

    override fun getFileName() = "fifteen.txt"

    private val directions = listOf(Direction.North, Direction.East, Direction.South, Direction.West)

    fun printShortestPath() {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
        val start = Point(0, 0)
        val found = mutableMapOf<Point, DisplayType>().apply { put(start, DisplayType.Start)}
        createMaze(found, comp, start)
        printKnownArea(found)
        shortestPath(found, start)
    }

    fun floodFill() {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
        val start = Point(0, 0)
        val found = mutableMapOf<Point, DisplayType>().apply { put(start, DisplayType.Start)}
        createMaze(found, comp, start)
        val oxyStart = found.filterValues { it == DisplayType.OxygenSystem }.keys.first()
        found[oxyStart] = DisplayType.Empty
        println(floodFill(found, oxyStart, -1))
    }

    private fun floodFill(maze: MutableMap<Point, DisplayType>, cur: Point, steps: Int): Int {
        printKnownArea(maze)
        return if (maze[cur] != DisplayType.Empty) steps
        else {
            maze[cur] = DisplayType.OxygenSystem
            var maxStep = -1
            for (direction in directions) {
                maxStep = max(floodFill(maze, getPoint(cur, direction), steps + 1), maxStep)
            }
            maxStep
        }
    }

    private fun shortestPath(found: MutableMap<Point, DisplayType>, cur: Point, steps: Int = 0): Boolean {
        val next = found.remove(cur)
        next?.let {
            if (it == DisplayType.OxygenSystem) println(steps)
            else if (it !=  DisplayType.Wall) {
                for (direction in directions) {
                    val nextPoint = getPoint(cur, direction)
                    if (found.contains(nextPoint)) {
                        if (shortestPath(found, nextPoint, steps + 1)) return true
                    }
                }
            }
        }
        return false
    }

    private fun createMaze(found: MutableMap<Point, DisplayType>, comp: LongCodeComputer, cur: Point) {
        for (direction in directions) {
            val nextPoint = getPoint(cur, direction)
            if (!found.contains(nextPoint)) {
                when (comp.parseAllInstructions(listOf(direction.d.toLong()), 1)[0].toInt()) {
                    0 -> found[nextPoint] = DisplayType.Wall
                    1 -> {
                        found[nextPoint] = DisplayType.Empty
                        createMaze(found, comp, nextPoint)
                        comp.parseAllInstructions(listOf(getOppositeDirection(direction).d.toLong()), 1)
                    }
                    2 -> {
                        found[nextPoint] = DisplayType.OxygenSystem
                        createMaze(found, comp, nextPoint)
                        comp.parseAllInstructions(listOf(getOppositeDirection(direction).d.toLong()), 1)
                    }
                }
            }
        }
    }

    private fun printKnownArea(found: MutableMap<Point, DisplayType>) {
        val xMin = found.minBy { it.key.x }!!.key.x
        val yMin = found.minBy { it.key.y }!!.key.y
        val yMax = found.maxBy { it.key.y }!!.key.y
        val xMax = found.maxBy { it.key.x }!!.key.x

        for (i in yMax downTo yMin) {
            for (j in xMin..xMax) {
                val type = found[Point(j, i)]
                type?.let { print(type.c) } ?: print("?")
            }
            println()
        }
    }

    private fun getPoint(cur: Point, dir: Direction): Point {
        return when (dir) {
            Direction.North -> Point(cur.x, cur.y + 1)
            Direction.South -> Point(cur.x, cur.y - 1)
            Direction.East -> Point(cur.x + 1, cur.y)
            Direction.West -> Point(cur.x - 1, cur.y)
        }
    }

    private fun getNextDirection(cur: Direction) = directionHelper(cur, 1)
    private fun getOppositeDirection(cur: Direction) = directionHelper(cur, 2)

    private fun directionHelper(cur: Direction, offset: Int): Direction {
        return directions[(directions.indexOf(cur) + offset) % directions.size]
    }

    private sealed class DisplayType(val c: Char) {
        object Wall: DisplayType('#')
        object Empty: DisplayType('.')
        object OxygenSystem: DisplayType('O')
        object Start: DisplayType('X')
    }

    private sealed class Direction(val d: Int) {
        object North: Direction(1)
        object South: Direction(2)
        object West: Direction(3)
        object East: Direction(4)
    }
}
