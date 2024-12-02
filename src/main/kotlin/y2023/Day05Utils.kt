package y2023

import kotlin.math.max
import kotlin.math.min

fun LongRange.length(): Long = last - first + 1

fun LongRange.overlaps(other: LongRange): Boolean {
    return !(this intersectedBy other).isEmpty()
}

fun LongRange.offsetBy(offset: Long): LongRange {
    return first + offset..last + offset
}

infix fun LongRange.intersectedBy(maskRange: LongRange): LongRange {
    return if (maskRange isFullyContainedIn this) maskRange
    else if (this isFullyContainedIn maskRange) this
    else max(first, maskRange.first)..min(last, maskRange.last)
}

fun LongRange.removeAll(other: List<LongRange>): List<LongRange> {
    return other.fold(listOf(this)) { ranges, range ->
        ranges.flatMap { it.remove(range) }
    }
}

fun LongRange.remove(other: LongRange): List<LongRange> {
    return if (this isFullyContainedIn other) emptyList()
    else if (this isFullyOutside other) listOf(this)
    else listOf(
        first until other.first,
        other.last + 1..last,
    ).filter { !it.isEmpty() }
}

infix fun LongRange.isFullyOutside(other: LongRange): Boolean {
    return last < other.first || first > other.last
}

infix fun LongRange.isFullyContainedIn(other: LongRange): Boolean {
    return first >= other.first && last <= other.last
}