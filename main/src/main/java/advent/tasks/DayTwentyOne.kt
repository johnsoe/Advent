package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer
import advent.helper.Util.Companion.toAscii

class DayTwentyOne : Day() {
    override fun getFileName(): String {
        return "twenty_one.txt"
    }

    fun walkHull(): Long {
        val instructions = listOf(
            "NOT A T",
            "NOT B J",
            "OR J T",
            "NOT C J",
            "OR T J",
            "AND D J",
            "WALK"
        )
        // (D && (!A || !B || !C))
        return traverseHelper(instructions)
    }

    fun traverseHelper(insts: List<String>): Long {
        val inputs = getInputBySeparator().map { it.toLong() }.toMutableList()
        val comp = LongCodeComputer(inputs)
        val instructions = insts.map { toAscii(it) }
            .reduce { acc, list -> acc + list }

        val outs = comp.parseAllInstructions(instructions)
        outs.forEach { print(it.toChar()) }
        return outs[outs.size-1]
    }

    fun runHull(): Long {
        val instructions = listOf(
            "NOT A T",
            "NOT B J",
            "OR J T",
            "NOT C J",
            "OR J T",
            "AND D T",
            "NOT I J",
            "NOT J J",
            "OR F J",
            "AND E J",
            "OR H J",
            "AND T J",
            "RUN"
        )
        // (D && (!A || !B || !C)) && (H || (E && (I || F)))
        return traverseHelper(instructions)
    }
}