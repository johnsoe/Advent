package advent.tasks.nineteen

class DayFour {

    fun findValidPassword(range: IntRange): Int {
        return range.filter {
            neverDecreases(it.toString()) && hasDouble(it.toString())
        }.size
    }

    fun findStrictPassword(range: IntRange): Int {
        return range.filter {
            neverDecreases(it.toString()) && hasStrictDouble(it.toString())
        }.size
    }

    private fun neverDecreases(pwd: String): Boolean {
        pwd.forEachIndexed { index, c ->
            if (index + 1 < pwd.length) {
                if (pwd[index] > pwd[index + 1]) {
                    return false
                }
            }
        }
        return true
    }

    private fun hasDouble(pwd: String): Boolean {
        pwd.forEachIndexed { index, c ->
            if (index + 1 < pwd.length) {
                if (pwd[index] == pwd[index + 1]) {
                    return true
                }
            }
        }
        return false
    }

    private fun hasStrictDouble(pwd: String): Boolean {
        var i = 0
        while (i + 1 < pwd.length) {
            var match = 0
            var j = i + 1
            while (j < pwd.length && pwd[j] == pwd[i]) {
                j++
                match++
            }
            if (match == 1) {
                return true
            }
            i += match + 1
        }
        return false
    }
}