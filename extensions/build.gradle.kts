
plugins {
    kotlin("jvm")
}

java.sourceSets["main"].java.srcDir("src")
java.sourceSets["test"].java.srcDir("test")


dependencies {
    compile(rootProject)
    compile(kotlin("stdlib-jdk8"))
}