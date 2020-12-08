package advent.tasks.twenty

import advent.helper.Day

class DayEight: Day() {
    override fun getFileName() = "twenty/eight.txt"

    fun accumulateWithInfiniteLoop(): Int {
        return helper(getInputByLine()).acc
    }

    private fun helper(input: List<String>, changedIndex: Int? = null): InstructionModel {
        var model = InstructionModel(0, 0)
        val seen = mutableSetOf<Int>()
        while (!seen.contains(model.index) && model.index < input.size) {
            seen.add(model.index)
            model = handleCmd(
                input[model.index],
                model,
                changedIndex == model.index
            )
        }
        return model
    }

    fun accumulateWithoutInfiniteLoop(): Int {
        val startInput = getInputByLine()
        startInput.forEachIndexed { index, _ ->
            helper(startInput, index).let {
                if (it.index >= startInput.size) {
                    return it.acc
                }
            }
        }
        return 0
    }

    private fun handleCmd(input: String, model: InstructionModel, isSwap: Boolean): InstructionModel {
        val split = input.split(" ")
        var delta = split[1].substring(1).toInt()
        if (split[1][0] == '-') {
            delta *= -1
        }
        val cmd = split[0]
        return when {
            isSwap && cmd == "nop" || (cmd == "jmp" && !isSwap) -> model.copy(index = model.index + delta)
            isSwap && cmd == "jmp" || cmd == "nop" -> model.copy(index = model.index + 1)
            cmd == "acc" -> model.copy(index = model.index + 1, acc = model.acc + delta)
            else -> {
                println("Unsupported command type: $cmd")
                model
            }
        }
    }

    private data class InstructionModel(
        val index: Int,
        val acc: Int
    )
}