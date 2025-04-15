import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    // JWT
    implementation("io.jsonwebtoken:jjwt:0.12.6")

    // OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Logging
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
