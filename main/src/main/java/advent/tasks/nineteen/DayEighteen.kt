package advent.tasks.nineteen

import advent.helper.Day
import advent.helper.Util
import java.awt.Point
import java.lang.IllegalStateException
import java.util.*

class DayEighteen : Day() {
    override fun getFileName(): String {
        return "eighteen.txt"
    }

    fun stepsForAllKeys(): Int {
        val maze = Maze()
        printMaze(maze)
        val distances = calculateMazeDistances(maze)
        distances.forEach {
            println(it.key.toString())
            it.value.forEach { p ->
                println("${p.first} ${p.second}")
            }
        }

        return 0
    }

    private fun calculateMazeDistances(maze: Maze): Map<MazeItem, Set<Pair<MazeItem, Int>>> {
        val items = mutableMapOf<MazeItem, Set<Pair<MazeItem, Int>>>()
        val startItem = maze.getItemFromPoint(maze.start)
        items[startItem] = getDistanceForItem(maze.start, maze)
        maze.doorMap.values.forEach {
            val item = maze.getItemFromPoint(it)
            items[item] = getDistanceForItem(it, maze)
        }
        maze.keyMap.values.map {
            val item = maze.getItemFromPoint(it)
            items[item] = getDistanceForItem(it, maze)
        }
        return items
    }

    private fun getDistanceForItem(start: Point, maze: Maze): Set<Pair<MazeItem, Int>> {
        val itemSet = mutableSetOf<Pair<MazeItem, Int>>()
        val seen = Array<BooleanArray>(maze.getHeight()) { BooleanArray(maze.getWidth()) { false } }
        var queue = LinkedList<Point>()
        queue.add(start)
        var stepCount = 0
        while (queue.isNotEmpty()) {
            val cur = queue.remove()
            seen[cur.y][cur.x] = true
            for (direction in Util.directions) {
                val next = Util.getNextPoint(cur, direction)
                if (next.x >= 0 && next.x < maze.getWidth() && next.y >= 0 && next.y < maze.getHeight()) {
                    if (maze.getItemFromPoint(next) != MazeItem.Wall && !seen[next.y][next.x]) {
                        queue.add(next)
                    }
                }
            }
            //TODO: Analyze current location.
            val curItem = maze.getItemFromPoint(cur)
            if (curItem is MazeItem.Door || curItem is MazeItem.Key || curItem is MazeItem.Start) {
                itemSet.add(Pair(curItem, stepCount))
            }
            stepCount++
        }
        return itemSet
    }

    private fun printMaze(maze: Maze) {
        maze.layout.forEach {
            it.forEach {item ->
                print(when (item) {
                    MazeItem.Start -> '@'
                    MazeItem.Wall -> '#'
                    MazeItem.Empty -> '.'
                    is MazeItem.Door -> item.name
                    is MazeItem.Key -> item.name
                })
            }
            println()
        }
        println()
    }

    inner class Maze {
        var layout: Array<Array<MazeItem>>
        val keyMap = mutableMapOf<Char, Point>()
        val doorMap = mutableMapOf<Char, Point>()
        var start = Point(0,0)

        init {
            val lines = getInputByLine()
            layout = Array(lines.size) { Array<MazeItem>(0) { MazeItem.Empty } }
            lines.forEachIndexed { yIndex, s ->
                layout[yIndex] = s.toCharArray().mapIndexed { xIndex, c ->
                    when {
                        c == '#' -> MazeItem.Wall
                        c == '.' -> MazeItem.Empty
                        c == '@' -> {
                            start = Point(xIndex, yIndex)
                            MazeItem.Start
                        }
                        c.isLowerCase() -> {
                            keyMap[c] = Point(xIndex, yIndex)
                            MazeItem.Key(c)
                        }
                        c.isUpperCase() -> {
                            doorMap[c] = Point(xIndex, yIndex)
                            MazeItem.Door(c)
                        }
                        else -> {
                            throw IllegalStateException("invalid char: $c")
                        }
                    }
                }.toTypedArray()
            }
        }

        fun getWidth(): Int = layout[0].size
        fun getHeight(): Int = layout.size
        fun getItemFromPoint(point: Point) = layout[point.y][point.x]
    }

    sealed class MazeItem {
        object Start : MazeItem()
        object Wall : MazeItem()
        object Empty : MazeItem()
        data class Key(val name: Char) : MazeItem()
        data class Door(val name: Char) : MazeItem()
    }
}