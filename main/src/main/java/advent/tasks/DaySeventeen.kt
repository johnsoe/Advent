package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer

class DaySeventeen : Day() {
    override fun getFileName(): String {
        return "seventeen.txt"
    }

    fun calculateAlignmentParameters(): Int {
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList())
        val outputs = comp.parseAllInstructions().map { it.toChar() }

        outputs.forEach { print(it) }

        val width = outputs.indexOfFirst { it.toInt() == 10 } + 1

        val intersections = mutableListOf<Int>()
        outputs.forEachIndexed { index, c ->
            if (c == '#') {
                try {
                    val neighbors = listOf(
                        outputs[index - width],
                        outputs[index - 1],
                        outputs[index + 1],
                        outputs[index + width]
                    )
                    if (neighbors.all { it == '#' }) {
                        intersections.add(index)
                    }
                } catch (e: IndexOutOfBoundsException) {}
            }
        }
        intersections.forEach { println(it) }
        return intersections.sumBy{ (it % width) * (it / width) }
    }

    fun traverseScaffolding() {
        val a =  toAscii("L,12,L,12,L,6,L,6")
        val b =  toAscii("R,8,R,4,L,12")
        val c =  toAscii("L,12,L,6,R,12,R,8")
        val main = toAscii("A,B,A,C,B,A,C,B,A,C")

        val combined = main + a + b + c + toAscii("n")
        println(combined)
        val comp = LongCodeComputer(getInputBySeparator().map { it.toLong() }.toMutableList().apply { set(0, 2) })
        val outputs = comp.parseAllInstructions(combined)
        println(outputs.last())
    }

    fun toAscii(input: String): List<Long> {
        return input.map { it.toLong() }.toMutableList().apply {
            add(10L)
        }
    }
}