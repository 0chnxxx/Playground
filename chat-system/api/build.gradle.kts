import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("kapt")
    kotlin("plugin.allopen")
    kotlin("plugin.jpa") version "1.9.25"
}

dependencies {
    implementation(project(":core"))

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // QueryDSL
    implementation("io.github.openfeign.querydsl:querydsl-core:6.10.1")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:6.10.1")
    kapt("io.github.openfeign.querydsl:querydsl-apt:6.10.1:jpa")

    // ObjectMapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = true
