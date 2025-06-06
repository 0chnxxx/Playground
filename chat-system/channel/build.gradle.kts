import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.allopen")
}

dependencies {
    implementation(project(":core"))

    // WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // ObjectMapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

allOpen {
    annotation("org.springframework.cloud.openfeign.FeignClient")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = true
