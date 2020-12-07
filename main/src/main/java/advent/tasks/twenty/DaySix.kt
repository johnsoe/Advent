package advent.tasks.twenty

import advent.helper.Day

class DaySix: Day() {
    override fun getFileName() = "twenty/six.txt"

    fun getUniqueResponseCount(): Int {
        return getInputByChunk().map {
            it.replace(" ", "").toSet().size
        }.sum()
    }

    fun getSharedResponseCount(): Int {
        return getInputByChunk().map {
            var charSet = mutableSetOf<Char>()
            val split = it.split(" ")
            charSet.addAll(split[0].toSet())
            split.forEach { word ->
                charSet = charSet.intersect(word.toSet()).toMutableSet()
            }
            charSet.size
        }.sum()
    }
}