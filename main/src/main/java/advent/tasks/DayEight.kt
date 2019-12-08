package advent.tasks

import advent.helper.Day

class DayEight: Day() {
    override fun getFileName(): String {
        return "eight.txt"
    }

    fun notSureAboutFuncNameHere(width: Int, height: Int): Int {
        val area = height * width
        val points = mutableMapOf<Int, MutableSet<LayerPoint>>()
        getInputByLine()[0].forEachIndexed { index, c ->
            val layer = index/area
            points.putIfAbsent(
                layer, mutableSetOf()
            )
            points[layer]!!.add(
                LayerPoint(
                    layer,
                    index % width,
                    (index / width) % height,
                    c.toInt() - 48
                )
            )
        }

        // Part 1
        val maxEntry = points.minBy { entry -> entry.value.filter { it.num == 0 }.size }
        val ones = maxEntry!!.value.filter { it.num == 1 }.size
        val twos = maxEntry.value.filter { it.num == 2 }.size
        println(ones * twos)

        // Part 2
        val pic = mutableListOf<Int>().apply {
            repeat(area) { add(2) }
        }
        points.forEach { entry ->
            println(entry.value.size)
            entry.value.forEach {
                println("${it.x} ${it.y}")
                val index = it.x + (it.y * width)
                if (pic[index] == 2 && it.num != 2) {
                    pic[index] = it.num
                }
            }
        }
        pic.forEachIndexed { index, i ->
            if (index % 25 == 0) {
                println()
            }
            if (i == 1) {
                print("#")
            } else {
                print(" ")
            }
        }
        return 0
    }

    private data class LayerPoint(
        val layer: Int,
        val x: Int,
        val y: Int,
        val num: Int
    )
}