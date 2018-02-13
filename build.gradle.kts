import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.1.51"
}

java.sourceSets["main"].java.srcDir("src/main")
java.sourceSets["test"].java.srcDir("src/test")

group = "xerus.mpris"

application {
    mainClassName = "xerus.mpris.MPRISPlayerKt"
}

repositories {
    jcenter()
}

dependencies {
    compile("com.github.hypfvieh", "dbus-java", "2.7.4")
    compile(kotlin("runtime"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
