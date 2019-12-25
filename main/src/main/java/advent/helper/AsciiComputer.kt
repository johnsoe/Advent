package advent.helper

class AsciiComputer(instructions: MutableList<Long>) : LongCodeComputer(instructions) {

    companion object {
        const val command = "Command?"
    }

    fun parseAllInstructions(input: String) : String {
        val longInput = Util.toAscii(input)
        val result = parseListInstructions(longInput, Util.toAscii(command))

        return Util.fromAscii(result).removeSuffix("]").removePrefix("[")
    }
}