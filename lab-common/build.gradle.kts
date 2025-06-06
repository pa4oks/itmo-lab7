plugins {
    `java-library`
}

dependencies {
    api(project(":db-core"))
    api("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.19.0")

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.logback.classic)
    testImplementation("org.mockito:mockito-core:5.17.0")
}

tasks.test {
    useJUnitPlatform()
}

