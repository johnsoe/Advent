package advent.helper

import java.io.File

abstract class Day {
    companion object {
        private const val INPUT_BASE = "main/src/main/java/advent/input/"
    }
    abstract fun getFileName(): String

    fun getInputByLine(): List<String> = File("$INPUT_BASE${getFileName()}").readLines()


}