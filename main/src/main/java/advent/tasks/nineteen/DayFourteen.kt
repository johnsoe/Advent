package advent.tasks.nineteen

import advent.helper.Day
import java.lang.Long.max
import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.math.abs

class DayFourteen : Day() {
    override fun getFileName(): String {
        return "fourteen.txt"
    }

    fun calcOneFuel(): Long {
        val reactions = getReactions()
        val matrix = Array(reactions.size) { LongArray(reactions.size + 1) { 0L } }
        val colMap = mutableMapOf<String, Int>()
        var index = 0
        var colIndex = 0
        for (reaction in reactions) {
            if (!colMap.containsKey(reaction.output.name)) {
                colMap[reaction.output.name] = colIndex
                colIndex++
            }
            matrix[index][colMap[reaction.output.name]!!] = reaction.output.quantity

            for (item in reaction.inputs) {
                if (!colMap.containsKey(item.name)) {
                    colMap[item.name] = colIndex
                    colIndex++
                }
                matrix[index][colMap[item.name]!!] = item.quantity * -1
            }
            index++
        }

        printMatrix(colMap, matrix)
        var ores = 0L
        var m = 3700000L
        while(ores < 1000000000000) {
            ores = solveMatrix(colMap, matrix.copyOf(), "ORE", "FUEL", m)
            m++
        }
        return ores
    }

    private fun solveMatrix(cols: Map<String, Int>, matrix: Array<LongArray>, input: String, output: String, m: Long): Long {
        val oreCol= cols[input]!!
        val fuelCol = cols[output]!!
        val fuelRow = matrix.indexOfFirst { it[fuelCol] > 0 }

        matrix[fuelRow] = matrix[fuelRow].map { it * m }.toLongArray()
        fun firstNegative(): Int {
            matrix[fuelRow].forEachIndexed { index, l ->
                if (l < 0 && index != oreCol) {
                    return index
                }
            }
            return -1
        }

        var colIndex = firstNegative()
        var count = 0
        while (colIndex != -1) {
            count++
            val row = matrix.first { it[colIndex] > 0 }
            val fuelRowChemAmt = matrix[fuelRow][colIndex]
            val testRowAmt = row[colIndex]
            var multiplier = abs(fuelRowChemAmt / testRowAmt)
            val mod = fuelRowChemAmt % testRowAmt
            if (mod != 0L || multiplier == 0L) multiplier++
            matrix[fuelRow].forEachIndexed { index, i ->
                matrix[fuelRow][index] += (row[index] * multiplier)
            }
            colIndex = firstNegative()
        }
        println(matrix[fuelRow][fuelCol])
        return abs(matrix[fuelRow][oreCol])
    }

    private fun printMatrix(cols: Map<String, Int>, matrix: Array<LongArray>) {
        val a = cols.toList().sortedBy { (_, value) -> value }
        val titleWidth = a.maxBy { it.first.length }!!.first.length.toLong()
        var chemWidth = 0L
        matrix.forEach {
            it.forEach { a ->
               chemWidth = max(a.toString().length.toLong(), chemWidth)
            }
        }
        val width = max(chemWidth, titleWidth) + 1
        cols.forEach {
            print(it.key)
            repeat((width - it.key.length).toInt()) { print(" ") }
        }
        println()
        matrix.forEach {
            it.forEach { r ->
                val digitCount = r.toString().length
                print(r)
                repeat((width - digitCount).toInt()) { print(" ") }
            }
            println()
        }
        println()
    }

    private fun getReactions(): Set<Reaction> {
        val allReactions = mutableSetOf<Reaction>()
        getInputByLine().forEach {
            val split = it.split(" ")
            val inputs = mutableListOf<Chemical>()
            var output: Chemical? = null
            for (i in 0..split.size step 2) {
                if (split[i] == "=>") {
                    output = Chemical(split[i + 2], split[i + 1].toLong())
                    break
                } else {
                    val name = split[i + 1].removeSuffix(",")
                    inputs.add(Chemical(name, split[i].toLong()))
                }
            }
            allReactions.add(Reaction(inputs, output!!))
        }

        val fuel = allReactions.find { it.output.name == "FUEL" }!!
        val queue = LinkedList<Chemical>()
        val ordered = LinkedHashSet<Reaction>()
        ordered.add(fuel)
        queue.addAll(fuel.inputs)
        while (queue.isNotEmpty()) {
            val next = queue.remove()
            allReactions.find { it.output.name == next.name }?.let {
                if (!ordered.contains(it)) {
                    ordered.add(it)
                }
                queue.addAll(it.inputs)
            }
        }
        return ordered
    }

    private data class Chemical(
        val name: String,
        var quantity: Long
    )

    private class Reaction(
        val inputs: List<Chemical>,
        val output: Chemical
    )
}