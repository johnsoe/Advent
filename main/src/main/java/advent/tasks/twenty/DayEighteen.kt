package advent.tasks.twenty

import advent.helper.Day
import java.util.*

class DayEighteen : Day() {
    override fun getFileName(): String {
        return "twenty/eighteen.txt"
    }

    val digitRegext = "[0-9]".toRegex()

    fun first(): Long {
        return getInputByLine().map {
            evaluateExpression(it.replace(" ", ""))
        }.sum()
    }

    private fun evaluateExpression(expression: String, deferMultiply: Boolean = false): Long {

        var result = 0L
        var op = '+'
        var stack = Stack<Long>()

        fun calcResult(exp: Long) {
            when (op) {
                '+' -> result += exp
                '*' -> result *= exp
            }
        }
        fun updateStack(toPush: Long) {
            stack.push(result)
            stack.push(toPush)
            result = 0L
            op = '+'
        }

        expression.forEach {
            when {
                (it == '*' && !deferMultiply) || it == '+' -> op = it
                digitRegext.matches(it.toString()) -> {
                    calcResult(Character.getNumericValue(it).toLong())
                }
                it == '*' -> updateStack(it.toLong())
                it == '(' -> {
                    stack.push(it.toLong())
                    updateStack(op.toLong())
                }
                it == ')' -> {
                    while(stack.peek().toChar() != '(') {
                        op = stack.pop().toChar()
                        calcResult(stack.pop())
                    }
                    stack.pop()
                }
            }
        }
        while (stack.isNotEmpty()) {
            op = stack.pop().toChar()
            val popped = stack.pop()
            calcResult(popped)
        }
        return result
    }

    fun second(): Long {
        return getInputByLine().map {
            evaluateExpression(it.replace(" ", ""), true)
        }.sum()
    }
}