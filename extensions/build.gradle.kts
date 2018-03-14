
plugins {
    application
    kotlin("jvm")
}

java.sourceSets["main"].java.srcDir("src")
java.sourceSets["test"].java.srcDir("test")

application {
    mainClassName = "xerus.mpris.DBusPropertyDelegateKt"
}

dependencies {
    compile(rootProject)
    compile(kotlin("stdlib"))
}