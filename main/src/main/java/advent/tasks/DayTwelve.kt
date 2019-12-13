package advent.tasks

import advent.helper.Day
import kotlin.math.abs

class DayTwelve: Day() {

    override fun getFileName() = "twelve.txt"

    fun calcTotalEnergy(steps: Int): Int {
        val moons = mutableSetOf<Moon>()
        getInputByLine().forEach { moons.add(createMoon(it))}
        repeat(steps) {
            moons.forEach { applyGravity(it, moons) }
            moons.forEach { applyVelocity(it) }
        }
        return calculateEnergy(moons)
    }

    fun calcCycle(): Long {
        val moons = mutableSetOf<Moon>()
        getInputByLine().forEach { moons.add(createMoon(it))}
        var steps = 0L
        while(!checkVelocitiesZero(moons) || steps == 0L) {
            moons.forEach { applyGravity(it, moons) }
            moons.forEach { applyVelocity(it) }
            steps++
            if (steps % 1000000 == 0L) {
                println("${steps / 1000000} million")
            }
        }
        return steps * 2
    }

    private fun checkVelocitiesZero(moons: MutableSet<Moon>): Boolean {
        return moons.all { moon ->
            moon.velocity.all {
                it == 0
            }
        }
    }

    private fun createMoon(init: String): Moon {
        val moon = Moon()
        val positions = init.split(",")
        positions.forEachIndexed { index, input ->
            moon.position[index] = getValueFromStr(index, input)
        }
        return moon
    }

    private fun getValueFromStr(index: Int, input: String): Int {
        val num = input.split("=")[1]
        return if (num.contains(">")) {
            num.substring(0, num.length - 1).toInt()
        } else {
            num.toInt()
        }
    }

    private fun applyGravity(moon: Moon, moons: MutableSet<Moon>) {
        moons.forEach {
            if (it != moon) {
                moon.position.forEachIndexed { index, _ ->
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
        moon.position.forEachIndexed { index, _ ->
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
            return position.sumBy { abs(it) } *
                velocity.sumBy { abs(it) }
        }
    }
}
