package advent.tasks

import advent.helper.Day
import java.awt.Point

class DayThree : Day() {

    override fun getFileName() = "three.txt"

    fun findLowestDistance() {
        val input = getInputByLine()

        val firstPath = getPathData(input[0].split(","))
        val secondPath = getPathData(input[1].split(","))

        val intersections = firstPath intersect secondPath
        val minPoint = intersections.minBy { findDistanceFromCenter(it) }
        println(findDistanceFromCenter(minPoint!!))
        println(findMinimumStepDistance(firstPath, secondPath, intersections) + 2)
    }

    private fun findDistanceFromCenter(point: Point): Int {
        return Math.abs(point.x) + Math.abs(point.y)
    }

    private fun getPathData(path: List<String>): List<Point> {
        val allPoints = mutableListOf<Point>()
        var temp = Point(0, 0)
        path.forEach { instruction ->
            val range = instruction.substring(1).toInt()
            repeat(range) {
                when (instruction[0]) {
                    'U' -> temp = Point(temp.x, temp.y + 1)
                    'D' -> temp = Point(temp.x, temp.y - 1)
                    'L' -> temp = Point(temp.x - 1, temp.y)
                    'R' -> temp = Point(temp.x + 1, temp.y)
                }
                allPoints.add(temp)
            }
        }
        return allPoints
    }

    private fun findMinimumStepDistance(a: List<Point>, b: List<Point>, i: Set<Point>): Int {
        val stepsA: MutableMap<Point, Int> = mutableMapOf()
        val stepsB: MutableMap<Point, Int> = mutableMapOf()

        i.forEach {
            stepsA[it] = Math.min(calculateSteps(a, it), stepsA[it] ?: Int.MAX_VALUE)
            stepsB[it] = Math.min(calculateSteps(b, it), stepsB[it] ?: Int.MAX_VALUE)
        }
        var min = Integer.MAX_VALUE
        i.forEach {
            min = Math.min(min, stepsA[it]!! + stepsB[it]!!)
        }
        return min
    }

    private fun calculateSteps(points: List<Point>, point: Point): Int {
        var sum = 0
        var prevPoint = Point(0, 0)
        for (i in 0..points.size) {
            if (points[i] == point) {
                break
            } else {
                sum += Math.abs(prevPoint.x - points[i].x) + Math.abs(prevPoint.y - points[i].y)
            }
            prevPoint = points[i]
        }
        return sum
    }
//    private fun processDirection(instruction: String): GridDirection {
//        return when (instruction[0]) {
//            'U' -> GridDirection.Up
//            'D' -> GridDirection.Down
//            'L' -> GridDirection.Left
//            'R' -> GridDirection.Right
//            else -> throw IllegalStateException("Invalid instruction: $instruction")
//        }
//    }
//
//    sealed class GridDirection {
//        object Up: GridDirection()
//        object Down: GridDirection()
//        object Left: GridDirection()
//        object Right: GridDirection()
//    }
}