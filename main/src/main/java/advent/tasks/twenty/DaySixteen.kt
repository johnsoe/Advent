package advent.tasks.twenty

import advent.helper.Day

class DaySixteen : Day() {
    override fun getFileName() = "twenty/sixteen.txt"

    fun getScanningErrorRate(): Int {
        val trainModel = getTrainModelData()
        return trainModel.nearbyTickets.map {
             getTicketErrors(it, trainModel.restrictions).sum()
        }.sum()
    }

    fun getDepartureSum(): Long {
        val trainModel = getTrainModelData()
        val validTickets = trainModel.nearbyTickets.filter {
            getTicketErrors(it, trainModel.restrictions).isEmpty()
        }
        val positionMap = mutableMapOf<Int, MutableList<TicketRestriction>>()
        trainModel.restrictions.forEachIndexed { index, _ ->
            positionMap[index] = trainModel.restrictions.toMutableList()
        }
        validTickets.forEach { fields ->
            fields.forEachIndexed { index, field ->
                positionMap[index]!!.removeAll {
                    !it.isValidForRanges(field)
                }
            }
        }

        val sorted = positionMap.toList().sortedBy { (_, value) -> value.size}.toMap()
        sorted.forEach {
            val toRemove = it.value.first()
            for (i in 0 until sorted.size) {
                if (i != it.key) {
                    positionMap[i]!!.remove(toRemove)
                }
            }
        }

        val indices = positionMap.filter {
            it.value.find { restriction ->
                restriction.name.contains("departure")
            } != null
        }.map { it.key }

        return trainModel.ticket
            .filterIndexed { index, _ -> index in indices }
            .reduce { acc, i -> acc * i }
    }

    private fun getTicketErrors(fields: List<Int>, restrictions: List<TicketRestriction>): List<Int> {
        return fields.filterNot {
            restrictions.any { restriction ->
                restriction.isValidForRanges(it)
            }
        }
    }

    private fun getTrainModelData(): TrainModel {
        val input = getInputByLine()
        val firstDiv = input.indexOfFirst { it.isEmpty() }
        val lastDiv = input.indexOfLast { it.isEmpty() }

        return TrainModel(
            generateRestrictions(input.subList(0, firstDiv)),
            input.subList(firstDiv + 2, lastDiv).first().split(",").map { it.toLong() },
            input.subList(lastDiv + 2, input.size).map { it.split(",").map { it.toInt() } }
        )
    }

    private fun generateRestrictions(restrictions: List<String>): List<TicketRestriction> {
        return restrictions.map {
            val split = it.split(": ")
            val rangeSplit = split.last().split(" or ")
            val ranges = rangeSplit.map { getRangeFromString(it) }
            TicketRestriction(
                split[0],
                ranges.first(),
                ranges.last()
            )
        }
    }

    private fun getRangeFromString(range: String): IntRange {
        val hypenIndex = range.indexOfFirst { it == '-' }
        val low = range.substring(0, hypenIndex).toInt()
        val high = range.substring(hypenIndex + 1).toInt()
        return low..high
    }

    private data class TicketRestriction(
        val name: String,
        val lowRange: IntRange,
        val highRange: IntRange
    ) {
        fun isValidForRanges(check: Int): Boolean {
            return check in lowRange || check in highRange
        }
    }

    private data class TrainModel(
        val restrictions: List<TicketRestriction>,
        val ticket: List<Long>,
        val nearbyTickets: List<List<Int>>
    )
}