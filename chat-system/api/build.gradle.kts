import org.springframework.boot.gradle.tasks.bundling.BootJar

val queryDslVersion: String by project

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = true

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":core"))

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Micrometer (Prometheus)
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // QueryDSL
    implementation("io.github.openfeign.querydsl:querydsl-core:$queryDslVersion")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
    kapt("io.github.openfeign.querydsl:querydsl-apt:$queryDslVersion:jpa")

    // Liquibase
    implementation("org.liquibase:liquibase-core")

    // ObjectMapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
