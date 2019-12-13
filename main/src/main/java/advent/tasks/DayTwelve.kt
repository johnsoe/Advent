

class DayTwelve: Day() {

    override fun getFileName() = "twelve.txt"

    fun calcTotalEnergy(steps: Int): Int {
        val moons = mutableSetOf<Moon>()
        getInputByLine().forEach { moons.add(createMoon(it))}
        repeat(steps) {
            moons.forEach {
                applyGravity(it, moons)
                applyVelocity(it)
            }
        }
        return calculateEnergy(moons)
    }

    private fun createMoon(init: String): Moon {
        val moon = Moon()
        val positions = init.split(",")
        positions.forEachIndexed { input, index ->
            moon.position[index] = getValueFromStr(index, input)
        }
        return moon
    }

    private fun getValueFromStr(index: Int, input: String): Int {
        return input.split("=")[1].toInt()
    }

    private fun applyGravity(moon: Moon, moons: MutableSet<Moon>) {
        moons.forEach {
            if (it != moon) {
                moon.position.forEachIndexed { _, index ->
                    moon.velocity[index] += compareMoons(
                        moon.position[index],
                        it.position[index]
                    )
                }
            }
        }
    }

    private fun compareMoons(first: Int, second: Int): Int {
        return when {
            first > second -> -1
            second > first -> 1
            else -> 0
        }
    }

    private fun applyVelocity(moon: Moon) {
        moon.position.forEachIndexed { _, index ->
            moon.position[index] += moon.velocity[index]
        }        
    }

    private fun calculateEnergy(moons: MutableSet<Moon>): Int {
        return moons.sumBy { it.getTotalEnergy() }
    }

    private class Moon {
        var position = intArrayOf(0, 0, 0)
        var velocity = intArrayOf(0, 0, 0)

        fun getTotalEnergy(): Int {
            return position.sumBy { Math.abs(it) } *
                velocity.sumBy { Math.abs(it) }
        }
    }
}
