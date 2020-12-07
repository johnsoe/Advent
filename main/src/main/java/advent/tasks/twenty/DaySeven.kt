package advent.tasks.twenty

import advent.helper.Day
import java.util.*

class DaySeven: Day() {
    override fun getFileName() = "twenty/seven.txt"

    fun getShinyBagPermutations(): Int {
        val sg = createBagGraph(false).getNodeByTitle("shiny gold")
        val seen = mutableSetOf<BagNode>()
        permutationHelper(sg, seen)
        return seen.size
    }

    private fun permutationHelper(node: BagNode, seen: MutableSet<BagNode>) {
        node.holds.map { it.node }
            .filter { !seen.contains(it) }
            .apply { seen.addAll(this) }
            .forEach { permutationHelper(it, seen) }
    }

    private fun createBagGraph(pointInward: Boolean): MutableSet<BagNode> {
        val graph = mutableSetOf<BagNode>()
        getInputByLine().forEach {
            val split = it.split(" ")
            if (split.size % 4 == 0) {
                val container = graph.getNodeByTitle("${split[0]} ${split[1]}")
                for (i in 4 until split.size step 4) {
                    val amount = split[i].toInt()
                    val contained = graph.getNodeByTitle("${split[i + 1]} ${split[i + 2]}")
                    if (pointInward) {
                        container.holds.add(
                            BagEdge(amount, contained)
                        )
                    } else {
                        contained.holds.add(
                            BagEdge(amount, container)
                        )
                    }
                }
            }
        }
        return graph
    }

    fun getCountOfInternalBags(): Int {
        val sg = createBagGraph(true).getNodeByTitle("shiny gold")
        return bagCountHelper(sg)
    }

    private fun bagCountHelper(node: BagNode): Int {
        return node.holds.map {
            it.amount * (bagCountHelper(it.node) + 1)
        }.sum()
    }

    private fun MutableSet<BagNode>.getNodeByTitle(title: String): BagNode {
        return this.find { it.title == title } ?: BagNode(title).also { this.add(it) }
    }

    private data class BagNode(
        val title: String,
        val holds: MutableSet<BagEdge> = mutableSetOf()
    )

    private data class BagEdge(
        val amount: Int,
        val node: BagNode
    )
}