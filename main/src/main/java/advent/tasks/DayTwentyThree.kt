package advent.tasks

import advent.helper.Day
import advent.helper.LongCodeComputer
import java.util.*
import kotlin.concurrent.thread

class DayTwentyThree : Day() {
    override fun getFileName(): String {
        return "twenty_three.txt"
    }
    private val networkQueue = Array<Queue<Long>>(50) { LinkedList<Long>() }
    private var nat: Pair<Long, Long>? = null
    private var natDelivery = mutableListOf<Long>()

    private val lock = Object()
    private var natUpdated = false
    private val hasSeen = BooleanArray(50) {false}

    fun startNetwork(): Int {
        val inputs = getInputBySeparator().map { it.toLong() }.toMutableList()
        for (i in 0..49) {
            thread {
                val comp = LongCodeComputer(inputs)
                val outputs = mutableListOf<Long>()
                while (true) {
                    comp.parseWithCallback(i, ::nextInput, outputs)
                    if (outputs.size == 3) {
                        val outIndex= outputs[0].toInt()
                        if (outIndex == 255) {
                            nat = Pair(outputs[1], outputs[2])
                            natUpdated = true
                            println("$i nat $nat")
                        } else {
                            synchronized(lock) {
                                println("$i ${outputs[0]} ${Pair(outputs[1], outputs[2])}")
                                networkQueue[outIndex].addAll(listOf(outputs[1], outputs[2]))
                            }
                        }
                        outputs.clear()
                    }
                }
            }
        }

        while (natDelivery.size < 2 || natDelivery.last() != natDelivery[natDelivery.size - 2]) {
            if (isIdle() && natUpdated) {
                natUpdated = false
                synchronized(lock) {
                    networkQueue[0].addAll(listOf(nat!!.first, nat!!.second))
                    natDelivery.add(nat!!.second)
                }
            }
        }
        return nat!!.second.toInt()
    }

    private fun isIdle(): Boolean {
        synchronized(lock) {
            return networkQueue.all { it.size == 0 }
        }
    }

    private fun nextInput(input: Int): Long {
        synchronized(lock) {
            return when {
                !hasSeen[input] -> {
                    hasSeen[input] = true
                    input.toLong()
                }
                networkQueue[input].isNotEmpty() -> {
                    networkQueue[input].remove()
                }
                else -> -1L
            }
        }
    }
}