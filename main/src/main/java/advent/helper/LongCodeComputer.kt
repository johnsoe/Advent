package advent.helper

class LongCodeComputer constructor(
    instructions: MutableList<Long>
) {

    var lastOpcode: Opcode? = null

    private var relativeIndex: Long = 0
    private var inputIndex = 0
    private var dataMap = mutableMapOf<Long, Long>()
    private var fullIndex = 0L

    init {
        instructions.forEachIndexed { index, i ->
            dataMap[index.toLong()] = i
        }
    }

    fun parseAllInstructions(inputs: List<Long> = listOf(), pauseOnOutputs: Int = 0): List<Long> {
        val output = mutableListOf<Long>()
        inputIndex = 0
        do {
            val op = parseNextInstruction(fullIndex, output, inputs)
            fullIndex = op.second
            lastOpcode = op.first
        } while (op.first != Opcode.Terminate && (pauseOnOutputs == 0 || output.size != pauseOnOutputs))
        return output
    }

    private fun parseNextInstruction(index: Long, output: MutableList<Long>, inputs: List<Long>): Pair<Opcode, Long> {
        val instruction = dataMap[index]!!
        val opcode = getOpCode(instruction)
        val params = getOpParams(instruction, opcode)

        fun nextLookUp(offset: Int): Long {
            return dataMap[lookup(index + offset, params[offset - 1])] ?: 0
        }
        fun update(offset: Int, set: Long) {
            dataMap[lookup(index + offset, params[offset - 1])] = set
        }
        when (opcode) {
            Opcode.Add -> {
                val result = nextLookUp(1) + nextLookUp(2)
                update(3, result)
            }
            Opcode.Multiply -> {
                val result = nextLookUp(1) * nextLookUp(2)
                update(3, result)
            }
            Opcode.Read -> {
                update(1, inputs[inputIndex])
                inputIndex++
            }
            Opcode.Write -> {
                output.add(nextLookUp(1))
            }
            Opcode.JumpIfTrue -> {
                if (nextLookUp(1) != 0L) {
                    return Pair(opcode, nextLookUp(2))
                }
            }
            Opcode.JumpIfFalse -> {
                if (nextLookUp(1) == 0L) {
                    return Pair(opcode, nextLookUp(2))
                }
            }
            Opcode.LessThan -> {
                val isLessThan = nextLookUp(1) < nextLookUp(2)
                update(3,  if (isLessThan) 1 else 0)
            }
            Opcode.Equals -> {
                val isEqual = nextLookUp(1) == nextLookUp(2)
                update(3,  if (isEqual) 1 else 0)
            }
            Opcode.AdjustRelative -> {
                val amountToAdjust = nextLookUp(1)
                relativeIndex += amountToAdjust
            }
            Opcode.Terminate -> { /* noop */ }
        }
        return Pair(opcode, opcode.paramCount + 1 + index)
    }

    private fun getOpParams(instruction: Long, opcode: Opcode): List<ParamCode> {
        var params = (instruction / 100).toInt()
        val paramList = mutableListOf<ParamCode>()
        repeat(opcode.paramCount) {
            paramList.add(
                when (params % 10) {
                    0 -> ParamCode.Position
                    1 -> ParamCode.Immediate
                    2 -> ParamCode.Relative
                    else -> throw IllegalStateException("Unexpected param code: ${params % 10}")
                }
            )
            params /= 10
        }
        return paramList
    }


    // Take bottom two digits of the instruction
    private fun getOpCode(instruction: Long): Opcode {
        val op = (instruction % 100).toInt()
        return when(op) {
            1 -> Opcode.Add
            2 -> Opcode.Multiply
            3 -> Opcode.Read
            4 -> Opcode.Write
            5 -> Opcode.JumpIfTrue
            6 -> Opcode.JumpIfFalse
            7 -> Opcode.LessThan
            8 -> Opcode.Equals
            9 -> Opcode.AdjustRelative
            99 -> Opcode.Terminate
            else -> throw IllegalStateException("Unsupported opcode $op")
        }
    }

    private fun lookup(index: Long, param: ParamCode): Long {
        return when (param) {
            ParamCode.Position -> dataMap[index] ?: 0
            ParamCode.Immediate -> index
            ParamCode.Relative -> (dataMap[index] ?: 0) + relativeIndex
        }
    }

    sealed class Opcode(val paramCount: Int) {
        object Add: Opcode(3)
        object Multiply: Opcode(3)
        object Read: Opcode(1)
        object Write: Opcode(1)
        object Terminate: Opcode(0)
        object JumpIfTrue: Opcode(2)
        object JumpIfFalse: Opcode(2)
        object LessThan: Opcode(3)
        object Equals: Opcode(3)
        object AdjustRelative: Opcode(1)
    }

    sealed class ParamCode {
        object Position: ParamCode()
        object Immediate: ParamCode()
        object Relative: ParamCode()
    }
}
