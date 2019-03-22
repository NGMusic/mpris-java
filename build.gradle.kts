plugins {
	kotlin("jvm") version "1.3.21"
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
	compile("com.github.hypfvieh", "dbus-java", "2.7.5")
	compile(kotlin("stdlib"))
}