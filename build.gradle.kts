import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.2.51"
}

java.sourceSets["main"].java.srcDir("src")

allprojects {
	group = "xerus.mpris"
	buildDir = rootDir.resolve("build")
	
	repositories {
		jcenter()
		maven("https://jitpack.io")
	}
	
}

val kotlinVersion: String by extra {
	buildscript.configurations["classpath"].resolvedConfiguration.firstLevelModuleDependencies
			.find { it.moduleName == "org.jetbrains.kotlin.jvm.gradle.plugin" }!!.moduleVersion
}

dependencies {
	compile("com.github.Xerus2000", "dbus-java", "2.7-SNAPSHOT")
	compile(kotlin("runtime"))
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}
}
