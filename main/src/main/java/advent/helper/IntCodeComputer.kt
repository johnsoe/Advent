package advent.helper

class IntCodeComputer constructor(
    val instructions: MutableList<Int>,
    private val init: Int
) {

    fun parseAllInstructions(): List<Int> {
        val output = mutableListOf<Int>()
        var index = 0
        do {
            val op = parseNextInstruction(index, output)
            index = op.second
        } while (op.first != Opcode.Terminate)
        return output
    }

    private fun parseNextInstruction(index: Int, output: MutableList<Int>): Pair<Opcode, Int> {
        val instruction = instructions[index]
        val opcode = getOpCode(instruction)
        val params = getOpParams(instruction, opcode)

        fun nextLookUp(offset: Int): Int {
            return instructions[lookup(index + offset, params[offset - 1])]
        }
        fun update(offset: Int, set: Int) {
            instructions[lookup(index + offset, params[offset - 1])] = set
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
            Opcode.Read -> update(1, init)
            Opcode.Write -> output.add(nextLookUp(1))
            Opcode.JumpIfTrue -> {
                if (nextLookUp(1) != 0) {
                    return Pair(opcode, nextLookUp(2))
                }
            }
            Opcode.JumpIfFalse -> {
                if (nextLookUp(1) == 0) {
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
            Opcode.Terminate -> { /* noop */ }
        }
        return Pair(opcode, opcode.paramCount + 1 + index)
    }

    private fun getOpParams(instruction: Int, opcode: Opcode): List<ParamCode> {
        var params = instruction / 100
        val paramList = mutableListOf<ParamCode>()
        repeat(opcode.paramCount) {
            paramList.add(
                when (params % 10) {
                    0 -> ParamCode.Position
                    1 -> ParamCode.Immediate
                    else -> throw IllegalStateException("Unexpected param code: ${params % 10}")
                }
            )
            params /= 10
        }
        return paramList
    }


    // Take bottom two digits of the instruction
    private fun getOpCode(instruction: Int): Opcode {
        val op = instruction % 100
        return when(op) {
            1 -> Opcode.Add
            2 -> Opcode.Multiply
            3 -> Opcode.Read
            4 -> Opcode.Write
            5 -> Opcode.JumpIfTrue
            6 -> Opcode.JumpIfFalse
            7 -> Opcode.LessThan
            8 -> Opcode.Equals
            99 -> Opcode.Terminate
            else -> throw IllegalStateException("Unsupported opcode $op")
        }
    }

    private fun lookup(index: Int, param: ParamCode): Int {
        return when (param) {
            ParamCode.Position -> instructions[index]
            ParamCode.Immediate -> index
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
    }

    sealed class ParamCode {
        object Position: ParamCode()
        object Immediate: ParamCode()
    }
}