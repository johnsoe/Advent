package advent.tasks

import advent.helper.AsciiComputer
import advent.helper.Day
import advent.helper.LongCodeComputer

class DayTwentyFive: Day() {
    override fun getFileName(): String {
        return "twenty_five.txt"
    }


    fun findShipPassword(): String {
        val comp = AsciiComputer(getInputBySeparator().map { it.toLong() }.toMutableList())

        var answer = ""
        while (true) {
            val out = comp.parseAllInstructions(answer)
            println(out)
            val items = getItems(out)
            println(items)
            val temp = readLine()!!
            answer = translate(temp, items)
        }
        return "password"
    }

    private fun getItems(input: String): List<String> {
        val lines = input.split("\n")
        val itemIndex = lines.indexOf("Items here:")
        val items = mutableListOf<String>()
        if (itemIndex != -1) {
            for (i in itemIndex + 1 until lines.size) {
                if (lines[i].startsWith("- ")) {
                    items.add(lines[i].removePrefix("- "))
                }
            }
        }
        return items
    }

    private fun translate(input: String, items: List<String>): String {
        return when (input) {
            "n" -> Command.North.str
            "s" -> Command.South.str
            "e" -> Command.East.str
            "w" -> Command.West.str
            "i" -> Command.Inventory.str
            "t" -> Command.Take(items.first()).str
//            "d" -> Command.Drop(items.first()).str
            else -> input

        }
    }

    private sealed class Command(val str: String) {
        object North: Command("north")
        object East: Command("east")
        object West: Command("west")
        object South: Command("south")
        data class Take(val item: String): Command("take $item")
        data class Drop(val item: String): Command("drop $item")
        object Inventory: Command("inv")
    }
}