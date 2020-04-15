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
	implementation(rootProject)
	implementation("org.slf4j", "slf4j-api", "1.7.25")
	testImplementation("com.github.xerus2000.util", "javafx", "2f67fc22d736818f53a04716814694dbb2c2fd40")
	testImplementation("org.slf4j", "slf4j-simple", "1.7.25")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}