import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

sourceSets {
	main {
		java.srcDir("src")
	}
	test {
		java.srcDir("test")
	}
}

dependencies {
	compile(rootProject)
	compile(kotlin("stdlib"))
	compile("org.slf4j", "slf4j-api", "1.7.25")
	testImplementation("com.github.xerus2000.util", "javafx", "24c9961a22c68df9345dd9d4e6d19e3d6a4d2f0a")
	testImplementation("org.slf4j", "slf4j-simple", "1.7.25")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}