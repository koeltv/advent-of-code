@file:Suppress("unused")
package utils

import java.math.BigInteger
import java.security.MessageDigest
import java.util.stream.IntStream
import java.util.stream.LongStream

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.printline() = println(this)

fun <V, R> compare(value: V, expected: R, lazyMessage: (V, R) -> String = { v, _ -> "Result is <$v>" }) {
    if (value?.equals(expected) == true) {
        println(lazyMessage(value, expected))
    } else {
        error("Expected <$expected>, actual $value")
    }
}

/**
 * Merge 2 [Map] with the same structure.
 * This uses a merge function to process cases where a key is present in both maps
 *
 * @param map2
 * @param mergeFunction
 * @receiver
 * @return
 */
fun <K, V> Map<K, V>.merge(map2: Map<K, V>, mergeFunction: (key: K, value1: V, value2: V) -> V): Map<K, V> {
    return this.filterKeys { it !in map2 } +
            map2.filterKeys { it !in this } +
            this.filterKeys { it in map2 }.mapValues { (key, value1) ->
                mergeFunction(key, value1, map2[key]!!)
            }
}

/**
 * Splits a list of elements on the given predicate, elements matching the predicate are thrown away.
 *
 * ```
 * val values = "1 2 3 4 5 6 7 8"
 * val usingSplit = values.split(" ")
 * val usingSplitOn = values.toList().splitOn { it == ' ' }.map { it.joinToString() }
 * ```
 *
 * @param T the type of elements in the list
 * @param predicate the predicate used to split the list
 * @receiver the list to be split
 * @return a list of lists, where each inner list represents a split sub-list
 */
fun <T> List<T>.splitOn(predicate: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, element ->
        if (predicate(element)) acc.apply { add(mutableListOf()) }
        else acc.apply { last().add(element) }
    }

fun LongRange.toStream(): LongStream {
    return if (step == 1L) LongStream.rangeClosed(first, last)
    else LongStream.iterate(first, { it <= last }) { it + step }
}

fun IntRange.toStream(): IntStream {
    return if (step == 1) IntStream.rangeClosed(first, last)
    else IntStream.iterate(first, { it <= last }) { it + step }
}

/**
 * Calculates the least common multiple (LCM) of the integers in the list.
 *
 * @receiver the list of integers to calculate LCM for
 * @return the LCM of the integers in the list
 */
fun List<Int>.lcm(): Long {
    return fold(1L) { lcm, value -> (lcm * value) / gcd(lcm, value.toLong()) }
}


/**
 * Calculates the greatest common divisor (GCD) of two numbers.
 *
 * @param a the first number
 * @param b the second number
 * @return the GCD of the two numbers
 */
fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

data class Coordinates(val x: Int, val y: Int) {
    fun near(coordinates: Coordinates): Boolean {
        return (-1..1).any { dx ->
            (-1..1).any { dy ->
                x == coordinates.x + dx && y == coordinates.y + dy
            }
        }
    }

    fun north(): Coordinates = Coordinates(x, y - 1)
    fun south(): Coordinates = Coordinates(x, y + 1)
    fun west(): Coordinates = Coordinates(x - 1, y)
    fun east(): Coordinates = Coordinates(x + 1, y)
    fun neighbors(): List<Coordinates> = listOf(north(), south(), west(), east())
    fun up(): Coordinates = north()
    fun down(): Coordinates = south()
    fun left(): Coordinates = west()
    fun right(): Coordinates = east()
    fun moveToward(direction: Direction): Coordinates = when (direction) {
        Direction.NORTH -> north()
        Direction.SOUTH -> south()
        Direction.WEST -> west()
        Direction.EAST -> east()
    }

    operator fun plus(coordinates: Coordinates): Coordinates {
        return Coordinates(x + coordinates.x, y + coordinates.y)
    }

    operator fun minus(coordinates: Coordinates): Coordinates {
        return Coordinates(x - coordinates.x, y - coordinates.y)
    }
}

/**
 * Checks whether the specified [point] belongs to the list of [CharSequence].
 *
 * A point belongs to the list of [CharSequence] if its coordinates are between (0, 0) and (this.lastIndex, this\[point.y].lastIndex).
 */
operator fun Collection<CharSequence>.contains(point: Coordinates): Boolean {
    return point.y in indices && point.x in first().indices
}

/**
 * Checks whether the specified [point] belongs to the 2D list.
 *
 * A point belongs to the 2D list if its coordinates are between (0, 0) and (this.lastIndex, this\[point.y].lastIndex).
 */
operator fun <T> List<List<T>>.contains(point: Coordinates): Boolean {
    return point.y in indices && point.x in this[point.y].indices
}

/**
 * Returns the element at the specified index in the list.
 * It is assumed that the first list contains lines and the second contains columns.
 */
operator fun <E> List<List<E>>.get(coordinates: Coordinates): E {
    return this[coordinates.y][coordinates.x]
}

operator fun List<CharSequence>.get(coordinates: Coordinates): Char {
    return this[coordinates.y][coordinates.x]
}

/**
 * Replaces the element at the specified position in this 2D list with the specified element.
 * It is assumed that the first list contains lines and the second contains columns.
 * @return the element previously at the specified position
 */
operator fun <E> MutableList<MutableList<E>>.set(coordinates: Coordinates, value: E): E {
    val previousValue = this[coordinates]
    this[coordinates.y][coordinates.x] = value
    return previousValue
}

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun opposite(): Direction {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }
    }

    fun isHorizontal(): Boolean = this == WEST || this == EAST

    fun rotateRight(): Direction {
        return Direction.entries[(this.ordinal + 1) % Direction.entries.size]
    }
}

fun <T> List<T>.withoutElementAt(index: Int) = this.filterIndexed { i, _ -> i != index }

fun IntRange.size(): Int = (last - first + 1) / step

fun String.splitOnNthChar(i: Int): Pair<String, String> = this.substring(0..(i - 1)) to this.substring(i, this.length)

inline fun <T, A: T, B: T> Pair<A, B>.forBoth(block: (T) -> Unit): Unit {
    block(this.first)
    block(this.second)
}