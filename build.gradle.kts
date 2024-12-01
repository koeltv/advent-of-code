import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jmh)
}

group = "com.koeltv"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.coroutine)
    testImplementation(libs.kotlin.test.junit)
}

jmh {
    providers.gradleProperty("select").orNull
        ?.split(',')
        ?.map { ".*$it.*" }
        ?.let { includes = it }

    //benchmarkMode = listOf("avgt")
    warmupIterations = 1
    iterations = 5
    fork = 1
}

kotlin {
    jvmToolchain(21)
}

val templatePaths = listOf(
    "source" to "src/main/kotlin/y\$year/Day\$dayNumber.kt",
    "test" to "src/test/kotlin/y\$year/Day\$dayNumberTest.kt",
    "input" to "src/main/resources/y\$year/Day\$dayNumber.txt",
    "input" to "src/main/resources/y\$year/Day\$dayNumber_test.txt",
)

val optionalTemplatePaths = listOf(
    "benchmark" to "src/jmh/kotlin/y\$year/Day\$dayNumberBenchmark.kt",
)

val yearProperty = providers.gradleProperty("year")
val dayProperty = providers.gradleProperty("day")

tasks.register("newYear") {
    description = "Creates the directories for an AOC year"
    group = "Advent of Code"

    val newYear = getYear()

    val sourceDir = layout.projectDirectory.dir("src/main/kotlin/y$newYear").asFile
    val testDir = layout.projectDirectory.dir("src/test/kotlin/y$newYear").asFile
    val ressourceDir = layout.projectDirectory.dir("src/resources/kotlin/y$newYear").asFile

    doLast {
        if (sourceDir.exists()) {
            println("$newYear was already setup")
        } else {
            sourceDir.mkdir()
            testDir.mkdir()
            ressourceDir.mkdir()

            println("Ready for $newYear !")
        }
    }
}

tasks.register("newDay") {
    description = "Creates all the necessary files for a new AOC day from the templates (sources, tests, input files)"
    group = "Advent of Code"

    val yearDirectory = getCurrentYearDirectory()
    val currentDay = getNextDay(yearDirectory)

    val properties = providers.gradlePropertiesPrefixedBy("").get()
    val selectedTemplatePaths = templatePaths + optionalTemplatePaths.filter { (template, _) -> template in properties }

    val currentYear = yearDirectory.name.drop(1)

    val templateMap = selectedTemplatePaths.map { (template, pathTemplate) ->
        val templateFile = layout.projectDirectory.file("templates/$template.txt").asFile
        val newFileName = pathTemplate.replace("\$year", currentYear).replace("\$dayNumber", currentDay)

        templateFile to layout.projectDirectory.file(newFileName).asFile
    }

    doLast {
        if (currentDay.toInt() > 25) {
            println("Cannot create a new day. Maximum number of days (25) reached.")
            return@doLast
        }

        templateMap.forEach { (template, pathTemplate) ->
            val filledTemplate = template.readText().replace("\$year", currentYear).replace("\$dayNumber", currentDay)

            pathTemplate.apply {
                parentFile.mkdirs()
                writeText(filledTemplate)
            }
        }

        println("Day n°$currentDay is ready !")
    }
}

tasks.register("newBenchmark") {
    description = "Creates a benchmark from a template for the given day and year"
    group = "Advent of Code"

    val year = getYear()
    val day = if (dayProperty.isPresent) getDay() else getCurrentDay(getCurrentYearDirectory())

    val sourceFile = layout.projectDirectory.file("src/main/kotlin/y$year/Day$day.kt").asFile

    val (template, newFile) = optionalTemplatePaths
        .first { (template, _) -> template == "benchmark" }
        .let { (template, pathTemplate) ->
            val templateFile = layout.projectDirectory.file("templates/$template.txt").asFile
            val newFile = pathTemplate.replace("\$year", year).replace("\$dayNumber", day)

            templateFile to layout.projectDirectory.file(newFile).asFile
        }

    doLast {
        if (!sourceFile.exists()) {
            error("There is no source file for Day $day of year $year")
        }

        val filledTemplate = template.readText().replace("\$year", year).replace("\$dayNumber", day)

        newFile.apply {
            parentFile.mkdirs()
            writeText(filledTemplate)
        }

        println("Day n°$day benchmark of year $year is ready !")
    }
}

fun getYear(): String {
    return if (yearProperty.isPresent) {
        yearProperty.get()
            .let { Regex("(\\d{4})").find(it)?.destructured?.component1() }
            ?: error("Year should be in the format <YYYY>, ex: -Pyear=2023")
    } else {
        LocalDate.now().year.toString()
    }
}

fun getDay(): String = dayProperty.get()
    .let { Regex("([1-9]|(1[0-9])|(2[0-5]))").find(it)?.destructured?.component1() }
    ?.padStart(2, '0')
    ?: error("Day should be between 1 and 25, ex: -Pday=1")

fun getCurrentDay(yearDirectory: File): String = if (dayProperty.isPresent) {
    getDay()
} else {
    if (!yearDirectory.exists()) "01"
    else {
        yearDirectory.listFiles()!!.let { directory ->
            if (directory.isEmpty()) "01"
            else directory
                .mapNotNull { Regex("Day(\\d{2})").find(it.name)?.groupValues?.get(1)?.toInt() }
                .max()
                .let { "$it".padStart(2, '0') }
        }
    }
}

fun getNextDay(yearDirectory: File): String {
    return if (!yearDirectory.exists()) "01"
    else {
        yearDirectory.listFiles()!!.let { directory ->
            if (directory.isEmpty()) "01"
            else getCurrentDay(yearDirectory).toInt().let { "${it + 1}".padStart(2, '0') }
        }
    }
}

fun getCurrentYearDirectory(): File {
    val sourceDir = layout.projectDirectory.dir("src/main/kotlin")
    val year = getYear()

    return if (yearProperty.isPresent) {
        sourceDir.dir("y$year").asFile
    } else {
        sourceDir.asFile
            .listFiles()!!
            .filter { Regex("y(\\d{4})").matches(it.name) }
            .maxByOrNull { it.name }
            ?: sourceDir.dir("y$year").asFile
    }
}