import org.springframework.boot.gradle.tasks.bundling.BootJar

val jwtVersion: String by project
val uuidCreatorVersion: String by project

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    // Spring Context
    implementation("org.springframework:spring-context")

    // JWT
    implementation("io.jsonwebtoken:jjwt:$jwtVersion")

    // Java UUID Generator (UUIDv7)
    implementation("com.github.f4b6a3:uuid-creator:$uuidCreatorVersion")

    // Logging
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
}
