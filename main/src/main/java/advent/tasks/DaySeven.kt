package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer

class DaySeven: Day() {

    override fun getFileName() = "seven_test.txt"

    fun findMaxThrust(): Long {
        val instructions = getInputBySeparator().map { it.toLong() }.toMutableList()
        return phaseOrderHelper(mutableListOf(), Long.MIN_VALUE, instructions, 0..4, ::calculateThrust)
    }

    fun findMaxFeedbackThrust(): Long {
        val instructions = getInputBySeparator().map { it.toLong() }.toMutableList()
        return calcThrustWithFeedback(instructions, mutableListOf(9,8,7,6,5))
        //return phaseOrderHelper(mutableListOf(), Integer.MIN_VALUE, instructions, 5..9, ::calcThrustWithFeedback)
    }

    private fun phaseOrderHelper(
        phases: MutableList<Int>,
        maxThrust: Long,
        instructions: MutableList<Long>,
        range: IntRange,
        thrustCalc: (MutableList<Long>, MutableList<Int>) -> Long
    ): Long {
        var max = maxThrust
        if (phases.size == 5) {
            println("$phases $maxThrust")
            val thrust = thrustCalc(instructions, phases)
            return Math.max(thrust, maxThrust)
        } else {
            for (i in range) {
                if (!phases.contains(i)) {
                    phases.add(i)
                    max = phaseOrderHelper(phases, max, instructions, range, thrustCalc)
                    phases.remove(i)
                }
            }
        }
        return max
    }

    private fun calculateThrust(instructions: MutableList<Long>, phases: MutableList<Int>): Long {
        var result = 0L
        phases.forEach {
            val comp = LongCodeComputer(instructions)
            result = comp.parseAllInstructions(listOf(it.toLong(), result))[0]
        }
        return result
    }

    private fun calcThrustWithFeedback(instructions: MutableList<Long>, phases: MutableList<Int>): Long {
        val size = phases.size
        val comps = mutableListOf<LongCodeComputer>()
        repeat(size, { comps.add(LongCodeComputer(instructions))})
        var i = 0
        var result = 0L
        while (true) {
            result = if (i < size) {
                comps[i % size].parseAllInstructions(listOf(phases[i].toLong(), result), true)[0]
            } else {
                val output = comps[i % size].parseAllInstructions(listOf(result), true)
                if (output.isNotEmpty()) {
                    output[0]
                } else {
                    result
                }
            }
            i++
            if (comps[i % size].lastOpcode == LongCodeComputer.Opcode.Terminate) {
                break
            }
        }
        return result
    }
}