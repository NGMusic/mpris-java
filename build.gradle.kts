import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.21"
}

sourceSets.main.get().java.srcDir("src")

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

tasks {
	withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}
}
