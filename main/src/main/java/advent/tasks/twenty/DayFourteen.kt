package advent.tasks.twenty

import advent.helper.Day

class DayFourteen: Day() {
    override fun getFileName() = "twenty/fourteen.txt"

    fun getSumOfAddressData(): Long {
        val storedData = mutableMapOf<String, Long>()
        var mask = ""
        getInstructions().forEach {
            when (it) {
                is Instruction.Mask -> mask = it.mask
                is Instruction.Store -> {
                    val padded = it.amt.toString(2)
                        .padStart(mask.length, '0')
                    storedData[it.addr] = applyMask(mask, padded).toLong(2)
                }
            }
        }
        return storedData.map { it.value }.sum()
    }

    private fun applyMask(mask: String, input: String, ignoreChar: Char = 'X'): String {
        return input.mapIndexed { index, c ->
            val maskChar = mask[index]
            if (maskChar == ignoreChar) {
                c
            } else {
                maskChar
            }
        }.joinToString("")
    }

    private fun getInstructions(): List<Instruction> {
        return getInputByLine().map { inst ->
            val split = inst.split(" = ")
            when (split[0]) {
                "mask" -> Instruction.Mask(split[1])
                else -> Instruction.Store(split[1].toLong(), split[0].substring(4, split[0].length - 1))
            }
        }
    }

    fun getSumWithAddressBitmask(): Long {
        var mask = ""
        val storedData = mutableMapOf<String, Long>()
        getInstructions().forEach { inst ->
            when (inst) {
                is Instruction.Mask -> mask = inst.mask
                is Instruction.Store -> {
                    val padded = inst.addr.toLong().toString(2)
                        .padStart(mask.length, '0')
                    generateMasks(applyMask(mask, padded, '0')).forEach {
                        storedData[it] = inst.amt
                    }
                }
            }
        }
        return storedData.map { it.value }.sum()
    }

    private fun generateMasks(mask: String): Set<String> {
        val masks = mutableSetOf<String>()
        maskGenHelper(mask, 0, masks)
        println(masks)
        return masks
    }

    private fun maskGenHelper(mask: String, index: Int, masks: MutableSet<String>) {
        if (!mask.contains('X') || index >= mask.length) {
            masks.add(mask)
        } else {
            if (mask[index] == 'X') {
                val front = mask.substring(0, index)
                val back = mask.substring(index + 1)
                maskGenHelper("${front}1$back", index + 1, masks)
                maskGenHelper("${front}0$back", index + 1, masks)
            } else {
                maskGenHelper(mask, index + 1, masks)
            }
        }
    }

    private sealed class Instruction {
        data class Mask(val mask: String): Instruction()
        data class Store(
            val amt: Long,
            val addr: String
        ): Instruction()
    }
}