package advent.tasks.twenty

import advent.helper.Day

class DayFour: Day() {
    override fun getFileName() = "twenty/four.txt"
    
    fun findValidPassportCount(): Int {
        return createPassportList().count { it.isValid() }
    }

    private fun createPassportList(): List<Passport> {
        return getInputByChunk().map {
            updatePassportFromInput(Passport(), it)
        }
    }

    fun findRestrictedPassportCount(): Int {
        return createPassportList().count { it.isValidWithRestrictions() }
    }

    private fun updatePassportFromInput(passport: Passport, input: String): Passport {
        input.split(" ").forEach {
            val pair = it.split(":")
            passport.setParam(pair.first(), pair.last())
        }
        return passport
    }

    private class Passport {
        var birth: String? = null
        var issue: String? = null
        var expiration: String? = null
        var height: String? = null
        var hairColor: String? = null
        var eyeColor: String? = null
        var passportId: String? = null
        var countryId: String? = null

        fun isValid(): Boolean {
            return !birth.isNullOrEmpty()
                && !issue.isNullOrEmpty()
                && !expiration.isNullOrEmpty()
                && !height.isNullOrEmpty()
                && !hairColor.isNullOrEmpty()
                && !eyeColor.isNullOrEmpty()
                && !passportId.isNullOrEmpty()
        }

        fun isValidWithRestrictions(): Boolean {
            return try {
                val isValidHeight = height?.let {
                    val heightNum = it.substring(0, it.length - 2).toInt()
                    when {
                        it.endsWith("in") -> heightNum in validInRange
                        it.endsWith("cm") -> heightNum in validCmRange
                        else -> false
                    }
                } ?: false

                isValidHeight
                    && birth?.toInt() in validBirthRange
                    && issue?.toInt() in validIssueRange
                    && expiration?.toInt() in validExpirationRange
                    && hairColor?.matches(validHairColor) ?: false
                    && eyeColor in validEyeColorSet
                    && passportId?.matches(validPassportId) ?: false
            } catch (e: Exception) {
                false // In case of invalid input.
            }
        }

        private val validBirthRange = 1920..2002
        private val validIssueRange = 2010..2020
        private val validExpirationRange = 2020..2030
        private val validCmRange = 150..193
        private val validInRange = 59..76
        private val validHairColor = "#[0-9a-f]{6}".toRegex()
        private val validEyeColorSet = setOf(
            "amb", "blu", "brn", "gry", "grn", "hzl", "oth"
        )
        private val validPassportId = "[0-9]{9}".toRegex()

        fun setParam(input: String, value: String) {
            when (input) {
                "byr" -> birth = value
                "iyr" -> issue = value
                "eyr" -> expiration = value
                "hgt" -> height = value
                "hcl" -> hairColor = value
                "ecl" -> eyeColor = value
                "pid" -> passportId = value
                "cid" -> countryId = value
                else -> {
                    println("Invalid input param: $input")
                }
            }
        }
    }
}