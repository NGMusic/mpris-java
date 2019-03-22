plugins {
	application
	kotlin("jvm")
}

sourceSets.main.get().java.srcDir("src")
sourceSets.main.get().java.srcDir("test")

application {
	mainClassName = "xerus.mpris.MPRISPlayerKt"
}

dependencies {
	compile(rootProject)
	compile(kotlin("stdlib"))
}