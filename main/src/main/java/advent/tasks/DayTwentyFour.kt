package advent.tasks

import advent.helper.Day
import java.lang.IllegalStateException
import kotlin.math.pow

class DayTwentyFour : Day() {
    override fun getFileName(): String {
        return "twenty_four.txt"
    }

    private val state = BugNode()

    fun smallVersion(): Int {
        val grid = defaultGrid()
        fillGrid(grid)
        printGrid(grid)
        while (!hasSeenPrevState(grid)) {
            writeState(grid)
            val nextState = nextGrid(grid)
            updateGrid(grid, nextState)
            printGrid(grid)
        }
        //too high 65022050
        return calcBioRating(grid)
    }

    private fun defaultGrid(): Array<BooleanArray>  = Array(5) { BooleanArray(5) { false } }

    private fun fillGrid(grid: Array<BooleanArray>) {
        getInputByLine().forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                grid[y][x] = when (c) {
                    '#' -> true
                    '.' -> false
                    else -> throw IllegalStateException("oops: $c")
                }
            }
        }
    }

    fun largeVersion(count: Int): Long {
        val grid = defaultGrid()
        fillGrid(grid)
        printGrid(grid)
        val grids = sortedMapOf<Int, Array<BooleanArray>>().apply {
            put(0, grid)
        }
        repeat(count) {
            val least = grids.firstKey()
            if (!gridIsEmpty(grids[least]!!)) {
                grids[least - 1] = defaultGrid()
            }
            val most = grids.lastKey()
            if (!gridIsEmpty(grids[most]!!)) {
                grids[most + 1] = defaultGrid()
            }
            val nextMap = mutableMapOf<Int, BugNode>()
            grids.keys.forEach {
                val nextState = nextGrid(grids[it]!!, grids[it + 1], grids[it - 1])
                nextMap[it] = nextState
            }
            grids.keys.forEach {
                updateGrid(grids[it]!!, nextMap[it]!!)
            }
        }
        grids.forEach {
            println("depth: ${it.key}")
            printGrid(it.value)
        }
        println()
        var total = 0L
        grids.keys.forEach {
            total += calcBioRating(grids[it]!!, false, false).toLong()
        }
        return total
    }

    private fun gridIsEmpty(grid: Array<BooleanArray>) = grid.all { it.all { b -> !b } }

    private fun printGrid(grid: Array<BooleanArray>) {
        grid.forEach {
            it.forEach { b ->
                print(if (b) '#' else '.')
            }
            println()
        }
        println()
    }

    private fun calcBioRating(grid: Array<BooleanArray>, power: Boolean = true, countCenter: Boolean = true): Int {
        var index = 0
        var total = grid.sumBy { booleans ->
            booleans.sumBy {
                index++
                if (it) {
                    if (power) 2.0.pow(index - 1).toInt() else 1
                } else {
                    0
                }
            }
        }
        if (grid[2][2] && !countCenter) {
            total -= if (power) 2.0.pow(12).toInt() else 1
        }
        return total
    }

    private fun nextGrid(grid: Array<BooleanArray>, outer: Array<BooleanArray>?, inner: Array<BooleanArray>?): BugNode {
        fun recursiveNeighborCount(x: Int, y: Int): Int {
            val outerTop = outer?.get(1)?.get(2)?.toInt() ?: 0
            val outerBottom = outer?.get(3)?.get(2)?.toInt() ?: 0
            val outerLeft = outer?.get(2)?.get(1)?.toInt() ?: 0
            val outerRight = outer?.get(2)?.get(3)?.toInt() ?: 0

            val innerLeft = inner?.sumBy { it[0].toInt() } ?: 0
            val innerRight = inner?.sumBy { it[4].toInt() } ?: 0
            val innerTop = inner?.get(0)?.sumBy { it.toInt() } ?: 0
            val innerBottom = inner?.get(4)?.sumBy { it.toInt() } ?: 0

            val top = when {
                y == 0 -> outerTop
                x == 2 && y == 3 -> innerBottom
                else -> grid[y - 1][x].toInt()
            }
            val bottom = when {
                y == grid.size - 1 -> outerBottom
                x == 2 && y == 1 -> innerTop
                else -> grid[y + 1][x].toInt()
            }
            val left = when {
                x == 0 -> outerLeft
                x == 3 && y == 2 -> innerRight
                else -> grid[y][x - 1].toInt()
            }
            val right = when {
                x == grid.size - 1 -> outerRight
                x == 1 && y == 2 -> innerLeft
                else -> grid[y][x + 1].toInt()
            }

            return top + bottom + left + right
        }

        return nextGrid(grid, ::recursiveNeighborCount)
    }

    private fun nextGrid(grid: Array<BooleanArray>, countFunc: ((Int, Int) -> Int)? = null): BugNode {
        val start = BugNode()
        var cur = start
        grid.forEachIndexed { y, booleans ->
            booleans.forEachIndexed { x, b ->
                val neighborCount = countFunc?.invoke(x, y) ?: getNeighborCount(grid, x, y)
                if (b && neighborCount == 1 || (!b && (neighborCount == 1 || neighborCount == 2))) {
                    cur.alive = BugNode()
                    cur = cur.alive!!
                } else {
                    cur.dead = BugNode()
                    cur = cur.dead!!
                }
            }
        }
        return start
    }

    private fun updateGrid(grid: Array<BooleanArray>, next: BugNode) {
        var cur = next
        grid.forEachIndexed { y, booleans ->
            booleans.forEachIndexed { x, _ ->
                grid[y][x] = when {
                    cur.dead != null -> {
                        cur = cur.dead!!
                        false
                    }
                    cur.alive != null -> {
                        cur = cur.alive!!
                        true
                    }
                    else -> throw IllegalStateException("WHUT")
                }
            }
        }
    }

    private fun getNeighborCount(grid: Array<BooleanArray>, x: Int, y: Int): Int {
        val top = if (y == 0) 0 else grid[y - 1][x].toInt()
        val bottom = if (y == grid.size - 1) 0 else grid[y + 1][x].toInt()
        val left = if (x == 0) 0 else grid[y][x - 1].toInt()
        val right = if (x == grid.size - 1) 0 else grid[y][x + 1].toInt()
        return top + bottom + left + right
    }

    fun Boolean.toInt() : Int {
        return if (this) 1 else 0
    }

    private fun writeState(grid: Array<BooleanArray>) {
        var cur = state
        grid.forEachIndexed { _, booleans ->
            booleans.forEachIndexed { _, cell ->
                if (cell) {
                    if (cur.alive == null) {
                        cur.alive = BugNode()
                    }
                    cur = cur.alive!!
                } else {
                    if (cur.dead == null) {
                        cur.dead = BugNode()
                    }
                    cur = cur.dead!!
                }
            }
        }
    }

    private fun hasSeenPrevState(grid: Array<BooleanArray>): Boolean {
        var cur = state
        grid.forEachIndexed { _, booleans ->
            booleans.forEachIndexed { _, cell ->
                val node = if (cell) cur.alive else cur.dead
                if (node == null) {
                    return false
                } else {
                    cur = node
                }
            }
        }
        return true
    }

    private class BugNode {
        var alive: BugNode? = null
        var dead: BugNode? = null
    }
}