package advent.helper

import java.io.File
import java.time.Year

abstract class Day() {
    companion object {
        private const val INPUT_BASE = "main/src/main/java/advent/input/"
    }
    abstract fun getFileName(): String

    fun getInputByLine(): List<String> = File("$INPUT_BASE${getFileName()}").readLines()
    fun getInputBySeparator(delimiter: String = ","): List<String> = getInputByLine().flatMap { it.split(delimiter) }
    fun getInputByChunk(): List<String> =
        getInputByLine().fold(mutableListOf<MutableList<String>>()) { acc, item ->
            if (item.isEmpty()) {
                acc.add(mutableListOf())
            } else {
                acc.lastOrNull()?.add(item) ?: acc.add(mutableListOf(item))
            }
            acc
        }.map { it.joinToString(separator = " ") }
}