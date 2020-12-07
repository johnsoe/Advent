package advent.tasks.twenty

import advent.helper.Day

class DaySix: Day() {
    override fun getFileName() = "twenty/six.txt"

    fun first(): Int {
        var charSet = mutableSetOf<Char>()
        var count = 0
        getInputByLine().forEach {
            if (it.isEmpty()) {
                count += charSet.size
                charSet = mutableSetOf()
            } else {
                charSet.addAll(it.toSet())
            }
        }
        return count + charSet.size
    }

    fun second(): Int {
        var charSet = mutableSetOf<Char>()
        var count = 0
        var firstLine = true
        getInputByLine().forEach {
            if (it.isEmpty()) {
                count += charSet.size
                charSet = mutableSetOf()
                firstLine = true
            } else {
                if (firstLine) {
                    charSet.addAll(it.toSet())
                } else {
                    charSet = charSet.intersect(it.toSet()).toMutableSet()
                }
                firstLine = false
            }
        }
        // 3319 got a weird error....Might be a solution to a different problem.
        return count + charSet.size
    }
}