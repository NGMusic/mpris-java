plugins {
	kotlin("jvm") version "1.3.71"
}

sourceSets.main.get().java.srcDir("src/main")
sourceSets.test.get().java.srcDir("src/test")

allprojects {
	group = "xerus.mpris"
	buildDir = rootDir.resolve("build")
	
	repositories {
		jcenter()
		maven("https://jitpack.io")
	}
	
}

dependencies {
	api("com.github.hypfvieh", "dbus-java", "3.0.2")
	api(kotlin("stdlib"))
}