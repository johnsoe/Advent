package advent.tasks.twenty

import advent.helper.Day

class DayThree: Day() {
    override fun getFileName() = "twenty/three.txt"

    fun getTreeEncounterCount(
        horizontalPerDrop: Int,
        dropAmount: Int = 1
    ): Int {
        var xPosition = -horizontalPerDrop
        return getInputByLine().withIndex().count {
            if (it.index % dropAmount == 0) {
                xPosition += horizontalPerDrop
                it.value[xPosition % it.value.length] == '#'
            } else {
                false
            }
        }
    }

    fun getMultipleForSlopes(): Int {
        return listOf(
            getTreeEncounterCount(1),
            getTreeEncounterCount(3),
            getTreeEncounterCount(5),
            getTreeEncounterCount(7),
            getTreeEncounterCount(1, 2)
        ).reduce { acc, i -> acc * i }
    }
}