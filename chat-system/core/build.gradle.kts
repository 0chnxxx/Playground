import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    // Spring Context
    implementation("org.springframework:spring-context")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.12.6")

    // Liquibase
    implementation("org.liquibase:liquibase-core")

    // Logging
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
