package advent.tasks

import advent.helper.Day

class DaySix: Day() {
    override fun getFileName() = "six.txt"

    companion object {
        private const val centerPlanet = "COM"
        private const val YOU = "YOU"
        private const val SANTA = "SAN"
    }

    fun calcTotalOrbits(): Int {
        val root = generateOrbitGraph()
        return orbitHelper(root, 0)
    }

    fun calcPlanetaryJumps(): Int {
        val root = generateOrbitGraph()

        val youPath = mutableListOf<String>()
        generatePath(root, YOU, youPath)

        val santaPath = mutableListOf<String>()
        generatePath(root, SANTA, santaPath)

        var index = 0
        while (true) {
            if (youPath[index] != santaPath[index]) {
                break
            }
            index++
        }
        return youPath.size + santaPath.size - ((index + 1) * 2)
    }

    private fun orbitHelper(root: Planet, depth: Int): Int {
        return if (root.orbiters.isEmpty()) {
            depth
        } else {
            root.orbiters.sumBy {
                orbitHelper(it, depth + 1)
            } + depth
        }
    }

    private fun generateOrbitGraph(): Planet {
        val allPlanets = mutableMapOf<String, Planet>()
        getInputByLine().forEach {
            val planetNames = it.split(")")
            val first = planetNames[0]
            val orbiter = planetNames[1]

            if (allPlanets[orbiter] == null) allPlanets[orbiter] = Planet(orbiter)
            if (allPlanets[first] == null) allPlanets[first] = Planet(first)
            allPlanets[first]!!.orbiters.add(allPlanets[orbiter]!!)
        }
        return allPlanets[centerPlanet] ?: throw IllegalStateException("Failed to have center planet")
    }

    private fun generatePath(root: Planet, search: String, path: MutableList<String>): Boolean {
        path.add(root.name)
        if (search != root.name) {
            root.orbiters.forEach {
                val result = generatePath(it, search, path)
                if (result) {
                    return true
                }
            }
        } else {
            return true
        }
        path.remove(root.name)
        return false
    }

    private class Planet(val name: String) {
        val orbiters = mutableSetOf<Planet>()
        override fun toString() = name
    }
}