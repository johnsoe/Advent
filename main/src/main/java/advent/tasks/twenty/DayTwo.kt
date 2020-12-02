package advent.tasks.twenty

import advent.helper.Day

class DayTwo : Day() {
    override fun getFileName() = "twenty/two.txt"

    fun getOldValidPasswordCount(): Int {
        return getInputByLine().count {
            getRuleFromInput(it).isValidForOldRules()
        }
    }

    fun getNewValidPasswordCount(): Int {
        return getInputByLine().count {
            getRuleFromInput(it).isValidForNewRules()
        }
    }

    private fun getRuleFromInput(input: String): PasswordRules {
        val split = input.split(" ")
        val minMax = split.first().split("-")
        return PasswordRules(
            minMax.first().toInt(),
            minMax.last().toInt(),
            split.last(),
            split[1][0]
        )
    }

    private data class PasswordRules(
        private val min: Int,
        private val max: Int,
        private val text: String,
        private val restriction: Char
    ) {
        fun isValidForOldRules(): Boolean {
            val repeatCount = text.count { it == restriction }
            return repeatCount in min..max
        }

        fun isValidForNewRules(): Boolean {
            var exactlyOne = false
            if (text[min - 1] == restriction) exactlyOne = !exactlyOne
            if (text[max - 1] == restriction) exactlyOne = !exactlyOne
            return exactlyOne
        }
    }
}