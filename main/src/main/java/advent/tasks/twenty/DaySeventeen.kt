package advent.tasks.twenty

import advent.helper.Day

class DaySeventeen : Day() {
    override fun getFileName() = "twenty/seventeen.txt"

    fun getActiveCountOnIteration(n: Int): Int {
        val space = createGrid(getInputByLine())
        for (i in 0 until n) {
            val nextGen = space.getNextGenState()
            space.iterateToNextGen(nextGen)
        }
        return space.getActiveCount()
    }

    fun getHyperspaceCountOnIteration(n: Int): Int {
        val hyperspace = Hyperspace().apply {
            spaces.add(createGrid(getInputByLine()))
        }
        for (i in 0 until n) {
            hyperspace.iterateToNextGen()
            //hyperspace.print()
        }
        return hyperspace.getActiveCount()
    }

    private fun createGrid(input: List<String>): Space {
        val grid = input.map { row ->
            row.map { state ->
                when (state) {
                    '#' -> true
                    '.' -> false
                    else -> {
                        throw IllegalStateException("Invalid state: $state")
                    }
                }
            }.toMutableList()
        }.toMutableList()
        return Space(
            mutableMapOf<Int, MutableList<MutableList<Boolean>>>().apply { put(0, grid) },
            0..0,
            grid[0].size,
            grid.size
        )
    }

    private data class Cell (
        val x: Int,
        val y: Int,
        val z: Int,
        val active: Boolean
    )

    private data class Space(
        // UGH         z        X & Y
        val cells: MutableMap<Int, MutableList<MutableList<Boolean>>>,
        var zBounds: IntRange,
        var rowSize: Int,
        var colSize: Int
    ) {
        fun getStateByCell(x: Int, y: Int, z: Int): Boolean {
            return if (z in zBounds && x >= 0 && y >= 0 && y < colSize && x < rowSize) {
                cells[z]!![y][x]
            } else {
                false
            }
        }

        fun setStateByCell(x: Int, y: Int, z: Int, active: Boolean) {
            cells[z]!![y][x] = active
        }

        fun getActiveCount(): Int {
            return zBounds.sumBy {
                cells[it]!!.sumBy { row ->
                    row.count { cell -> cell }
                }
            }
        }

        fun iterateToNextGen(nextGen: List<Cell>) {
            nextGen.forEach {
                setStateByCell(it.x, it.y, it.z, it.active)
            }
        }

        fun getNextGenState(countSelf: Boolean = false): List<Cell> {
            expand()
            val nextCells = mutableListOf<Cell>()
            zBounds.forEach { z ->
                cells[z]!!.forEachIndexed { rI, row ->
                    row.forEachIndexed { cI, c ->
                        val active = getStateByCell(cI, rI, z)
                        val count = getNeighborActiveCount(cI, rI, z, countSelf)
                        val activeNext = (active && count in 2..3) || (!active && count == 3)
                        nextCells.add(Cell(cI, rI, z, activeNext))
                    }
                }
            }
            return nextCells
        }

        fun getNeighborActiveCount(x0: Int, y0: Int, z0: Int, countSelf: Boolean): Int {
            var count = if (!countSelf && getStateByCell(x0, y0, z0)) -1 else 0
            for (x in x0 - 1..x0 + 1) {
                for (y in y0 - 1..y0 + 1) {
                    for (z in z0 - 1..z0 + 1) {
                        if (getStateByCell(x, y, z)) {
                            count++
                        }
                    }
                }
            }
            return count
        }

        fun expand() {
            rowSize += 2
            colSize += 2
            zBounds.forEach { z ->
                cells[z]!!.forEach { row ->
                    row.add(false)
                    row.add(0, false)
                }
                cells[z]!!.add(List(rowSize) { false }.toMutableList())
                cells[z]!!.add(0, List(rowSize) { false }.toMutableList())
            }
            zBounds = zBounds.first - 1..zBounds.last + 1
            createEmptyLayer(zBounds.first)
            createEmptyLayer(zBounds.last)
        }

        private fun createEmptyLayer(depth: Int) {
            cells[depth] = List(colSize) {
                List(rowSize) { false }.toMutableList()
            }.toMutableList()
        }

        fun print() {
            zBounds.forEach { z ->
                cells[z]!!.forEach { row ->
                    row.forEach { cell ->
                        if (cell) {
                            print('#')
                        } else {
                            print('.')
                        }
                    }
                    println()
                }
                println()
            }
        }

        companion object {
            fun createEmpty(x: Int, y: Int, z: IntRange): Space {
                val empty = Space(
                    mutableMapOf(),
                    z, x, y
                )
                z.forEach {
                    empty.createEmptyLayer(it)
                }
                return empty
            }
        }

    }

    private data class Hyperspace(
        val spaces: MutableList<Space> = mutableListOf()
    ) {
        fun iterateToNextGen() {
            getNextGenState().forEach {
                spaces[it.key].iterateToNextGen(it.value)
            }
        }
        fun print() {
            spaces.forEach {
                it.print()
                println()
            }
        }

        fun getActiveCount() = spaces.sumBy { it.getActiveCount() }

        fun getNextGenState(): Map<Int, MutableList<Cell>> {
            expand()
            val nextCells = mutableMapOf<Int, MutableList<Cell>>()
            spaces.forEachIndexed { sI, space ->
                val spaceCells = mutableListOf<Cell>()
                space.zBounds.forEach { z ->
                    space.cells[z]!!.forEachIndexed { rI, row ->
                        row.forEachIndexed { cI, c ->
                            val active = space.getStateByCell(cI, rI, z)
                            var spaceCount = space.getNeighborActiveCount(cI, rI, z, false)
                            if (sI > 0) {
                               spaceCount += spaces[sI - 1].getNeighborActiveCount(cI, rI, z, true)
                            }
                            if (sI < spaces.size - 1) {
                               spaceCount += spaces[sI + 1].getNeighborActiveCount(cI, rI, z, true)
                            }
                            val activeNext = (active && spaceCount in 2..3) || (!active && spaceCount == 3)
                            spaceCells.add(Cell(cI, rI, z, activeNext))
                        }
                    }
                }
                nextCells[sI] = spaceCells
            }
            return nextCells
        }

        private fun expand() {
            spaces.forEach { it.expand() }
            val space = spaces.first()
            spaces.add(
                Space.createEmpty(space.rowSize, space.colSize, space.zBounds)
            )
            spaces.add(0 ,
                Space.createEmpty(space.rowSize, space.colSize, space.zBounds)
            )
        }
    }
}