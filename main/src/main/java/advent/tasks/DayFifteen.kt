
class DayFifteen : Day() {

    override fun getFileName() = "fifteen.txt"

    private val directions = listOf(Direction.North, Direction.East, Direction.South, Direction.West)

    fun findShortestPath(): Int {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
        val found = mutableSetOf<Point, DisplayType>().apply { put(Point(0, 0), DisplayType.Empty)}
        return findShortestPath(found, comp, Point(0, 0))
    }

    private fun findShortestPath(found: MutableSet<Point, DisplayType>, comp: LongCodeComputer, cur: Point): Boolean {
        for (direction in directions) {
            val nextPoint = getPoint(cur, direction)
            if (!found.contains(nextPoint)) {
                val output = comp.parseAllInstructions(listOf(direction.d), 1)

                when (output) {
                    0 -> found[nextPoint] = DisplayType.Wall
                    1 -> {
                        found[nextPoint] = DisplayType.Empty
                        val foundPath = findShortestPath(found, comp, nextPoint)
                        if (foundPath) return true
                        comp.parseAllInstructions(listOf(getOppositeDirection(direction).d), 1)
                    }
                    2 -> {
                        found[nextPoint] = DisplayType.OxygenSystem
                        return true
                    }
                }
            }
        }
        return false
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
        return directions[(direction.indexOf(cur) + offset) % directions.size]
    }

    private sealed class DisplayType(c: Char) {
        object Wall: DisplayType('#')
        object Empty: DisplayType(' ')
        object OxygenSystem: DisplayType('O')
    }

    private sealed class Direction(d: Int) {
        object North: Direction(1)
        object South: Direction(2)
        object West: Direction(3)
        object East: Direction(4)
    }
}
