plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.playground.chat"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.12.6")

    // Logging
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")

    // Spring Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    getByName<Jar>("jar") {
        enabled = false
    }
    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
