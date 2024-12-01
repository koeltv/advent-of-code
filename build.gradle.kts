import java.time.LocalDate

val coroutineVersion: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.koeltv"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

jmh {
    properties["select"]?.toString()
        ?.split(',')
        ?.map { ".*$it.*" }
        ?.let { includes = it }

    //benchmarkMode = listOf("avgt")
    warmupIterations = 1
    iterations = 5
    fork = 1
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

val sourceDirectory = File("./src/main/kotlin")

// Setup everything for the current year
tasks.register("newYear") {
    doLast {
        val newYear =
            if (project.hasProperty("year")) getYear()?.toInt() ?: error("Wrong format for year (should be YYYY)")
            else LocalDate.now().year

        if (File("./src/main/kotlin/y$newYear").exists()) {
            println("$newYear was already setup")
        } else {
            File("./src/main/kotlin/y$newYear").mkdir()
            File("./src/test/kotlin/y$newYear").mkdir()
            File("./src/resources/kotlin/y$newYear").mkdir()

            println("Ready for $newYear !")
        }
    }
}

val templatePaths = listOf(
    "source" to "src/main/kotlin/y\$year/Day\$dayNumber.kt",
    "test" to "src/test/kotlin/y\$year/Day\$dayNumberTest.kt",
    "input" to "./src/main/resources/y\$year/Day\$dayNumber.txt",
    "input" to "./src/main/resources/y\$year/Day\$dayNumber_test.txt",
)

val optionalTemplatePaths = listOf(
    "benchmark" to "./src/jmh/kotlin/y\$year/Day\$dayNumberBenchmark.kt",
)

// Setup everything for a new day (source file, test file, empty input files)
tasks.register("newDay") {
    doLast {
        val yearDirectory = if (project.hasProperty("year")) {
            File("src/main/kotlin/y${getYear()!!}")
        } else {
            sourceDirectory.listFiles()!!
                .filter { Regex("y(\\d{4})").matches(it.name) }
                .maxByOrNull { it.name }
                ?: error("No package found with the format \"yYYYY\" (ex: \"y2023\")")
        }

        val currentYear = yearDirectory.name.drop(1)

        val currentDay = if (project.hasProperty("day")) {
            getDay() ?: error("Wrong format for day (should be between 1 and 25)")
        } else {
            yearDirectory.listFiles()!!.let { directory ->
                if (directory.isEmpty()) "01"
                else directory.mapNotNull { Regex("Day(\\d{2})").find(it.name)?.groupValues?.get(1)?.toInt() }
                    .maxOf { it + 1 }
                    .let { "$it".padStart(2, '0') }
            }
        }

        if (currentDay.toInt() > 25) {
            println("Cannot create a new day. Maximum number of days (25) reached.")
            return@doLast
        }

        val selectedTemplatePaths =
            templatePaths + optionalTemplatePaths.filter { (template, _) -> properties.contains(template) }

        selectedTemplatePaths.forEach { (template, directory) ->
            val templateFile = File("./templates/$template.txt")
                .readText()
                .replace("\$year", currentYear)
                .replace("\$dayNumber", currentDay)

            val newFile = directory.replace("\$year", currentYear).replace("\$dayNumber", currentDay)
            File(newFile).apply {
                parentFile.mkdirs()
                writeText(templateFile)
            }
        }

        println("Day n°$currentDay is ready !")
    }
}

// Create benchmark with a year and/or day passed
tasks.register("newBenchmark") {
    doLast {
        val year = getYear() ?: error("You need to specify a year via -Pyear")
        val day = getDay() ?: error("You need to specify a day via -Pday")

        if (!File("src/main/kotlin/y$year/Day$day.kt").exists()) {
            error("There is no source file for Day $day of year $year")
        }

        optionalTemplatePaths
            .first { (template, _) -> template == "benchmark" }
            .let { (template, directory) ->
                val templateFile = File("./templates/$template.txt")
                    .readText()
                    .replace("\$year", year)
                    .replace("\$dayNumber", day)

                val newFile = directory.replace("\$year", year).replace("\$dayNumber", day)
                File(newFile).apply {
                    parentFile.mkdirs()
                    writeText(templateFile)
                }
            }

        println("Day n°$day benchmark of year $year is ready !")
    }
}

fun getYear(): String? = properties["year"]
    ?.toString()
    ?.let { Regex("(\\d{4})").find(it)?.destructured?.component1() }

fun getDay(): String? = properties["day"]
    ?.toString()
    ?.let { Regex("([1-9]|(1[0-9])|(2[0-5]))").find(it)?.destructured?.component1() }
    ?.padStart(2, '0')