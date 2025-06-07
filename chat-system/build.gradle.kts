import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectGroup: String by project
val projectVersion: String by project
val javaVersion: String by project
val jvmVersion: String by project
val springCloudVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

allprojects {
    group = projectGroup
    version = projectVersion

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.valueOf(javaVersion)
        targetCompatibility = JavaVersion.valueOf(javaVersion)
    }

    tasks.withType<KotlinCompile> {
        kotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.valueOf(jvmVersion))
                freeCompilerArgs.add("-Xjsr305=strict")
            }
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    dependencies {
        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        testImplementation("org.jetbrains.kotlin:kotlin-test")

        // Kotlin Reflection
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // Spring Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}
