package advent.tasks

import advent.helper.Day

class DayTwo : Day() {

    override fun getFileName() = "two_a.txt"

    fun calcGravityAssist(noun: Int, verb: Int) : Int {
        getInputByLine().first().split(",").map {
            it.toInt()
        }.toMutableList().let {
            initList(it, noun, verb)
            var index = 0
            while(isValidOpcode(it, index)) {
                index += 4
            }
            it.forEach { num -> print("$num ") }
            println()
            return it[0]
        }
    }

    fun calcSpecificOutput(target: Int): Pair<Int, Int>? {
        for (i in 0..99) {
            for (j in 0..99) {
                if (calcGravityAssist(i, j) == target) {
                    return Pair(i, j)
                }
            }
        }
        return null
    }

    private fun initList(fullCode: MutableList<Int>, noun: Int, verb: Int) {
        fullCode[1] = noun
        fullCode[2] = verb
    }

    private fun isValidOpcode(fullCode: MutableList<Int>, index: Int): Boolean {
        val opcode = fullCode[index]
        if (index > fullCode.size - 3) {
            return false
        }
        return when (opcode) {
            1 -> {
                fullCode[fullCode[index + 3]] = fullCode[fullCode[index + 1]] + fullCode[fullCode[index + 2]]
                true
            }
            2 -> {
                fullCode[fullCode[index + 3]] = fullCode[fullCode[index + 1]] * fullCode[fullCode[index + 2]]
                true
            }
            99 -> false
            else -> {
                println("Ya goofed: $opcode $index")
                false
            }
        }
    }
}