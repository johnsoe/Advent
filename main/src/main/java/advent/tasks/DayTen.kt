package advent.tasks

import advent.helper.Day
import kotlin.math.atan2

class DayTen: Day() {
    override fun getFileName(): String {
        return "ten.txt"
    }

    fun partOne(): Int {
        val best = createAsteroidSet().maxBy { it.otherAsteroids.size }!!
        return best.otherAsteroids.size
    }

    private fun createAsteroidSet(): MutableSet<Asteroid> {
        val asteroids = mutableSetOf<Asteroid>()
        getInputByLine().forEachIndexed { aIndex, s ->
            val locations = s.split("")
            locations.forEachIndexed { lIndex, c ->
                if (c == "#") {
                    val asteroid = Asteroid(lIndex - 1, aIndex)
                    asteroids.forEach {
                        if (hasLineOfSight(it, asteroid, asteroids)) {
                            it.otherAsteroids.add(asteroid)
                            asteroid.otherAsteroids.add(it)
                        }
                    }
                    asteroids.add(asteroid)
                }
            }
        }
        return asteroids
    }

    private fun hasLineOfSight(first: Asteroid, second: Asteroid, all: Set<Asteroid>): Boolean {
        var xDiff = second.x - first.x
        var yDiff = second.y - first.y

        var gcm = Math.abs(gcm(xDiff, yDiff))
        if (gcm == 0) gcm = 1
        xDiff /= gcm
        yDiff /= gcm

        var tempX = first.x + xDiff
        var tempY = first.y + yDiff
        while(tempX != second.x || tempY != second.y) {
            if (all.any { it.x == tempX && it.y == tempY }) return false
            tempX += xDiff
            tempY += yDiff
        }
        return true
    }

    private fun gcm(a: Int, b: Int): Int {
        return if (b == 0) a else gcm(b, a % b)
    }

    fun partTwo(): Int {
        val destroyedAsteroids = mutableListOf<Asteroid>()
        val all = createAsteroidSet()
        val start = all.maxBy { it.otherAsteroids.size }!!
        println(start)
        val allWithAngle = calcAngles(start, all)
        var angle = atan2(-234.1, -1.0)
        while (allWithAngle.isNotEmpty()) {
            val next = getNextAsteroid(start, allWithAngle, angle)!!
            angle = allWithAngle[next]!!
            println(angle * 180 / Math.PI)
            destroyedAsteroids.add(next)
            allWithAngle.remove(next)
            println(next)
        }
        val a = destroyedAsteroids[200]
        println(a)
        return a.x * 100 + a.y
    }

    private fun printGrid(asteroids: Map<Asteroid, Double>) {
        for (i in 0..24) {
            for (j in 0..24) {
                var found = false
                asteroids.keys.forEach {
                    if (it.x == j && it.y == i) {
                        found = true
                    }
                }
                print(if (found) "#" else ".")
            }
            println()
        }
        println()
    }

    private fun getNextAsteroid(center: Asteroid, asteroids: Map<Asteroid, Double>, angle: Double): Asteroid? {
        asteroids.forEach {
            if (it.value > angle && hasLineOfSight(center, it.key, asteroids.keys)) {
                return it.key
            }
        }
        asteroids.forEach {
            if (hasLineOfSight(center, it.key, asteroids.keys)) {
                return it.key
            }
        }
        return null
    }

    private fun calcAngles(start: Asteroid, asteroids: Set<Asteroid>): MutableMap<Asteroid, Double> {
        return asteroids.associateBy ( {it}, {calcAngle(start, it)} ).toList().sortedBy { (key, value) -> value }.toMap().toMutableMap()
    }

    private fun calcAngle(a: Asteroid, b: Asteroid): Double {
        val xDiff = (b.x - a.x).toDouble()
        val yDiff = (b.y - a.y).toDouble()
        return atan2(yDiff, xDiff)
    }

    private class Asteroid(
        val x: Int,
        val y: Int
    ) {
        val otherAsteroids = mutableSetOf<Asteroid>()

        override fun toString(): String {
            return "$x $y"
        }
    }
}