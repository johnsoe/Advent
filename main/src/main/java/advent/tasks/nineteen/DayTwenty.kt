package advent.tasks.nineteen

import advent.helper.Day
import advent.helper.Util
import advent.helper.Util.Companion.getNextPoint
import java.awt.Point
import java.util.*

class DayTwenty : Day() {
    override fun getFileName(): String {
        return "twenty.txt"
    }

    fun traverseSteps(): Int{
        val maze = createMaze()
        bfsTraverse(maze)
        return maze.stepsToSolve
    }

    private fun bfsTraverse(maze: Maze) {
        var cur = Triple(maze.start, 0, 0)
        var nextSpots: Queue<Triple<Point, Int, Int>> = LinkedList()
        while (cur.first != maze.end || cur.third != 0) {
            val p = cur.first
            if (p.y >= 0 && p.y < maze.height() && p.x >= 0 && p.x < maze.width() && !maze.hasVisited(p, cur.third)) {
                println(cur)
                maze.setDistance(cur.first, cur.second, cur.third)
                val spot = maze.layout[p.y][p.x]
                when {
                    isPortal(spot) -> {
                        val outer = isOuterPortal(spot, cur.first, maze)
                        if (cur.third != 0 || !outer) {
                            val nextLevel = if (outer) cur.third - 1 else cur.third + 1
                            val next = maze.travelPortal(p, spot)
                            if (maze.hasVisited(next, nextLevel)) {
                                checkNeighbors(maze, cur, nextSpots)
                            } else {
                                nextSpots.add(Triple(next, cur.second + 1, nextLevel))
                            }
                        }
                    }
                    !isWall(spot) && spot != ' ' -> { checkNeighbors(maze, cur, nextSpots)}
                }
            }
            cur = nextSpots.remove()
        }
        maze.printMaze()
        maze.stepsToSolve = cur.second
    }

    private fun checkNeighbors(maze: Maze, cur: Triple<Point, Int, Int>, nextSpots: Queue<Triple<Point, Int, Int>>) {
        for (direction in Util.directions) {
            val next = getNextPoint(cur.first, direction)
            nextSpots.add(Triple(next, cur.second + 1, cur.third))
        }
    }

    private fun createMaze(): Maze {
        val lines = getInputByLine()
        val maze = Array(lines.size) { CharArray(0) }
        lines.forEachIndexed { index, s ->
            maze[index] = s.toCharArray()
        }
        val portals = mutableMapOf<Char, Portal>()
        val start = Point()
        val end = Point()
        maze.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (isPortal(c)) {
                    if(portals.containsKey(c)) {
                        portals[c]!!.doors.add(Point(x, y))
                    } else {
                        portals[c] = Portal().apply { doors.add(Point(x, y)) }
                    }
                } else if (isStart(c)) {
                    start.x = x
                    start.y = y
                } else if (isEnd(c)) {
                    end.x = x
                    end.y = y
                }
            }
        }

        return Maze(start, end, portals, maze)
    }

    private fun isPortal(c: Char): Boolean {
        return c != ' ' && c != '.' && !isWall(c) && !isStart(c) && !isEnd(c)
    }

    private fun isOuterPortal(c: Char, p: Point, maze: Maze): Boolean {
        return isPortal(c) && (p.x == 0 || p.y == 0 || p.x == maze.width() - 1 || p.y == maze.height() - 1)
    }

    private fun isWall(c: Char, level: Int = 0) = c == '#' || (c == 'Z' && level != 0)
    private fun isStart(c: Char) = c == 'A'
    private fun isEnd(c: Char) = c == 'Z'

    private class Maze(
        val start: Point,
        val end: Point,
        val portals: Map<Char, Portal>,
        val layout: Array<CharArray>
    ) {
        var stepsToSolve: Int = -1
        private var distance: MutableList<Array<IntArray>> = mutableListOf()
        init {
            addLevel()
        }

        fun travelPortal(start: Point, portal: Char): Point {
            val entry = portals[portal]
            return entry?.let {
                it.doors.find { door -> door != start }
            } ?: start
        }

        fun height() = layout.size
        fun width() = layout[0].size

        fun printMaze() {
            layout.forEachIndexed { y, chars ->
                chars.forEachIndexed { x, c ->
                    if (c == '.') print(distance[0][y][x] % 10)
                    else print(c)
                }
                println()
            }
            println()
            layout.forEach { arr ->
                arr.forEach {
                    print(it)
                }
                println()
            }
        }

        fun hasVisited(point: Point, level: Int = 0): Boolean {
            if (level == distance.size) {
                addLevel()
                println("${distance.size} $point")
            }
            return distance[level][point.y][point.x] != 0
        }
        fun setDistance(point: Point, d: Int, level: Int = 0) { distance[level][point.y][point.x] = d }

        private fun addLevel() {
            distance.add(Array(height()) { IntArray(width()) { 0 } })
        }
    }

    private data class Portal(
        val doors: MutableSet<Point> = mutableSetOf()
    )
}