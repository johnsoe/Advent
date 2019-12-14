
class DayEleven: Day() {

    override fun getFileName() = "eleven.txt"

    fun traverse(): Int {
        val input = getInputBySeparator().map { it.toLong() }.toMutableList()
        val comp = LongCodeComputer(input)

        var position = Point(0, 0)
        var direction = Direction.Up
        val paintedPositions = mutableMapOf<Point, Int>()

        while (comp.lastOpcode != Opcode.Terminate) {
            val nextInput = paintedPositions[position] ?: 0
            val outputs = comp.parseAllInstructions(listOf(nextInput)).map { it.toInt() }
            if (outputs.size == 2) {
                paintedPositions[position] = outputs[0]
                direction = getNextDirection(outputs[1], direction)
                step(position, direction)
            }
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
            Direction.Up -> Direction.Left
            Direction.Left -> Direction.Down
            Direction.Down -> Direction.Right
            Direction.Right -> Direction.Up
        }
    }

    private fun getRightDirection(prev: Direction) {
        getLeftDirection(getLeftDirection(getLeftDirection(prev)))
    }

    private fun step(current: Point, facing: Direction) {
        when (facing) {
            Direction.Up -> current.y++
            Direction.Down -> current.y--
            Direction.Left -> current.x--
            Direction.Right -> current.x++
        }
    }

    sealed class Direction(left: Direction, right: Direction) {
        object Up: Direction()
        object Down: Direction()
        object Left: Direction()
        object Right: Direction()
    }
}
