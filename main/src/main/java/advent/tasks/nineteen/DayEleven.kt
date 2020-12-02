import advent.helper.Day
import advent.helper.LongCodeComputer
import advent.helper.Util.Direction
import java.awt.Point

class DayEleven: Day() {

    override fun getFileName() = "eleven.txt"

    fun traverse(): Int {
        val input = getInputBySeparator().map { it.toLong() }.toMutableList()
        val comp = LongCodeComputer(input)

        var position = Point(0, 0)
        var direction : Direction = Direction.North
        val paintedPositions = mutableMapOf<Point, Int>()
        var step = 0

        while (comp.lastOpcode != LongCodeComputer.Opcode.Terminate) {
            val nextInput = paintedPositions[position] ?: if (step == 0) 1 else 0
            val outputs = comp.parseAllInstructions(listOf(nextInput.toLong()), 2).map { it.toInt() }
            if (outputs.size == 2) {
                paintedPositions[Point(position.x, position.y)] = outputs[0]
                direction = getNextDirection(outputs[1], direction)
                takeStep(position, direction)
            }
            step++
        }
        actuallyPaint(paintedPositions)
        return paintedPositions.size
    }

    private fun actuallyPaint(all: MutableMap<Point, Int>) {
        val xMin = all.minBy { it.key.x }!!.key.x
        val yMin = all.minBy { it.key.y }!!.key.y
        val yMax = all.maxBy { it.key.y }!!.key.y
        val xMax = all.maxBy { it.key.x }!!.key.x

        for (i in yMax downTo yMin) {
            for (j in xMin..xMax) {
                val color = all[Point(j, i)]
                color?.let {
                    print(if (it == 0) "." else "#")
                } ?: print(".")
            }
            println()
        }
    }

    private fun getNextDirection(turn: Int, prev: Direction): Direction {
        return when (turn) {
            0 -> getLeftDirection(prev)
            1 -> getRightDirection(prev)
            else -> throw IllegalStateException("ya goofed")
        }
    }

    private fun getLeftDirection(prev: Direction): Direction {
        return when (prev) {
            Direction.North -> Direction.West
            Direction.West -> Direction.South
            Direction.South -> Direction.East
            Direction.East -> Direction.North
        }
    }

    private fun getRightDirection(prev: Direction): Direction {
        return getLeftDirection(getLeftDirection(getLeftDirection(prev)))
    }

    private fun takeStep(current: Point, facing: Direction) {
        when (facing) {
            Direction.North -> current.y++
            Direction.South -> current.y--
            Direction.West -> current.x--
            Direction.East -> current.x++
        }
    }
}
