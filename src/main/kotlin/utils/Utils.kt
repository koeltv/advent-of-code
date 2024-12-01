package utils

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <V, R> compare(value: V, expected: R, lazyMessage: (V, R) -> String = { v, _ -> "Result is <$v>" }) {
    if (value?.equals(expected) == true) {
        println(lazyMessage(value, expected))
    } else {
        error("Expected <$value>, actual $expected")
    }
}