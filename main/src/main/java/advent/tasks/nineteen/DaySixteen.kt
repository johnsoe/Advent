package advent.tasks.nineteen

import advent.helper.Day
import java.lang.IllegalStateException
import java.lang.StringBuilder
import kotlin.math.abs

class DaySixteen : Day() {
    override fun getFileName(): String {
        return "sixteen.txt"
    }

    fun phaseTransmission(steps: Int): String {
        return phaseTransmission(steps, getInputByLine()[0])
    }

    private fun phaseTransmission(steps: Int, input: String): String {
        var output = input
        for (i in 0 until steps) {
            output = getNextStep(output)
            println(output)
        }
        return output
    }

    fun findSignal(steps: Int, repeat: Int): String {
        val builder = StringBuilder()
        val input = getInputByLine()[0]
        repeat(repeat) {
            builder.append(input)
        }
        val offset = input.substring(0, 7).toInt()
        val fullString = builder.toString().substring(offset)
        return reverseTransmission(steps, fullString)
    }

    private fun reverseTransmission(steps: Int, input: String): String {
        var output = input
        for (i in 0 until steps) {
            output = reverseTransmission(output)
            println(output)
        }
        return output
    }

    private fun reverseTransmission(input: String): String {
        var sum = 0
        var out = StringBuilder()
        for (i in input.length - 1 downTo 0) {
            sum += input[i].toInt() - 48
            out.append(sum % 10)
        }
        return out.reverse().toString()
    }

    private fun getNextStep(input: String): String {
        var out = StringBuilder()
        for (i in input.indices) {
            var sum = 0
            for (j in input.indices) {
                sum += getPhasedOutput(i ,j, input[j].toInt() - 48)
            }
            out.append(abs(sum % 10))
        }
        return out.toString()
    }

    private fun getPhasedOutput(phase: Int, index: Int, value: Int): Int {
        val phasedIndex = index % ((phase + 1) * 4)
        val shifted = ((phasedIndex + 1) / (phase + 1)) % 4
        return when (shifted) {
            0, 2 -> 0
            1 -> value
            3 -> value * -1
            else -> throw IllegalStateException("ya goofed $shifted")
        }
    }
}