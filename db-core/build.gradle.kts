plugins {
    `java-library`
}

dependencies {
    api(libs.slf4j.api)
    api("jakarta.inject:jakarta.inject-api:2.0.1")
    runtimeOnly(libs.logback.classic)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.logback.classic)
    testImplementation(libs.junit.jupiter)
    testImplementation("org.mockito:mockito-core:5.17.0")
    testImplementation("com.h2database:h2:2.3.232")
}

tasks.test {
    useJUnitPlatform()
}
