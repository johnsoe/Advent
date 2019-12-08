package advent.tasks

import advent.helper.Day
import advent.helper.IntCodeComputer

class DaySeven: Day() {

    override fun getFileName() = "seven_test.txt"

    fun findMaxThrust(): Int {
        val instructions = getInputBySeparator().map { it.toInt() }.toMutableList()
        return phaseOrderHelper(mutableListOf(), Integer.MIN_VALUE, instructions, 0..4, ::calculateThrust)
    }

    fun findMaxFeedbackThrust(): Int {
        val instructions = getInputBySeparator().map { it.toInt() }.toMutableList()
        return calcThrustWithFeedback(instructions, mutableListOf(9,8,7,6,5))
        //return phaseOrderHelper(mutableListOf(), Integer.MIN_VALUE, instructions, 5..9, ::calcThrustWithFeedback)
    }

    private fun phaseOrderHelper(
        phases: MutableList<Int>,
        maxThrust: Int,
        instructions: MutableList<Int>,
        range: IntRange,
        thrustCalc: (MutableList<Int>, MutableList<Int>) -> Int
    ): Int {
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

    private fun calculateThrust(instructions: MutableList<Int>, phases: MutableList<Int>): Int {
        var result = 0
        phases.forEach {
            val comp = IntCodeComputer(instructions)
            result = comp.parseAllInstructions(listOf(it, result))[0]
        }
        return result
    }

    private fun calcThrustWithFeedback(instructions: MutableList<Int>, phases: MutableList<Int>): Int {
        val size = phases.size
        val comps = mutableListOf<IntCodeComputer>()
        repeat(size, { comps.add(IntCodeComputer(instructions))})
        var i = 0
        var result = 0
        while (true) {
            result = if (i < size) {
                comps[i % size].parseAllInstructions(listOf(phases[i], result), true)[0]
            } else {
                val output = comps[i % size].parseAllInstructions(listOf(result), true)
                if (output.isNotEmpty()) {
                    output[0]
                } else {
                    result
                }
            }
            i++
            if (comps[i % size].lastOpcode == IntCodeComputer.Opcode.Terminate) {
                break
            }
        }
        return result
    }
}