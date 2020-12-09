package advent.tasks.twenty

object Util {

    fun getSummedPair(nums: Set<Long>, target: Long): Pair<Long, Long>? {
        return nums.firstOrNull { nums.contains(target - it) }?.let {
            it to (target - it)
        }
    }
}