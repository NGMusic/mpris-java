
plugins {
    kotlin("jvm")
}

java.sourceSets["main"].java.srcDir(".")

dependencies {
    compile(rootProject)
    compile(kotlin("stdlib"))
}