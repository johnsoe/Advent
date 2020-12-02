package advent.tasks.nineteen

import advent.helper.Day
import advent.helper.LongCodeComputer

class DayNineteen : Day() {
    override fun getFileName(): String {
        return "nineteen.txt"
    }

    fun tractorBeamArea(size: Int): Int {
        val inputs = getInputBySeparator().map { it.toLong() }.toMutableList()
        val area = Array(size) { IntArray(size) }

        area.forEachIndexed { i, arr ->
            arr.forEachIndexed { j, _ ->
                val comp = LongCodeComputer(inputs)
                val outs = comp.parseAllInstructions(listOf(j.toLong(), i.toLong()))
                area[i][j] = outs[0].toInt()
            }
        }
        printArea(area)
        return area.sumBy { it.count{ item -> item == 1 } }
    }

    private fun printArea(area: Array<IntArray>) {
        area.forEach {
            it.forEach { item ->
                if (item != 0) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun findShipArea(size: Int): Int {
        val inputs = getInputBySeparator().map { it.toLong() }.toMutableList()
        val rowList = mutableListOf<Row>()

        var depth = 0
        var prevMax = 0
        var prevMin = 0
        while (true) {
            var foundBeamStart = false
            for(i in prevMin..depth) {
                val comp = LongCodeComputer(inputs)
                val out = comp.parseAllInstructions(listOf(i.toLong(), depth.toLong()))[0].toInt()
                if (out == 1 && !foundBeamStart) {
                    foundBeamStart = true
                    prevMin = i
                } else if (out == 0 && foundBeamStart) {
                    prevMax = i - 1
                    break
                }
            }
            val row = Row(prevMin, prevMax)
            if (depth > size) {
                val prev = rowList[depth - size + 1]
                if (prev.end - row.start + 1 == size) {
                    rowList.forEachIndexed { index, l -> println("$index $l") }
                    return row.start * 10000 + depth - size + 1
                }
            }
            rowList.add(depth, row)
            depth++
        }
    }

    private class Row (
        val start: Int,
        val end: Int
    ) {
        override fun toString(): String {
            return "$start $end"
        }
    }
}