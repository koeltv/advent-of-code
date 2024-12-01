/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: Int, name: String) = object {}::class.java
    .getResourceAsStream("y$year/${name}.txt")!!
    .bufferedReader()
    .readLines()