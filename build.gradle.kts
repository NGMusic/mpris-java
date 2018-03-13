import com.jfrog.bintray.gradle.BintrayExtension
import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date

plugins {
    kotlin("jvm") version "1.2.21"
    id("com.jfrog.bintray") version "1.8.0"
}

java.sourceSets["main"].java.srcDir("src")

allprojects {
    group = "xerus.mpris"
    buildDir = rootDir.resolve("build")

    repositories {
        jcenter()
    }

}

val kotlinVersion: String? by extra {
    buildscript.configurations["classpath"].resolvedConfiguration.firstLevelModuleDependencies
            .find { it.moduleName == "org.jetbrains.kotlin.jvm.gradle.plugin" }?.moduleVersion
}

dependencies {
    compile("com.github.hypfvieh", "dbus-java", "2.7.4")
    compile("org.jetbrains.kotlin", "kotlin-runtime", kotlinVersion)
}

bintray {
    user = "xerus"
    key = "7cc1023426ab266d37501eb6b714732839a63814"

    pkg(closureOf<BintrayExtension.PackageConfig>{
        repo = "mpris-java"
        name = "mpris"
        version = VersionConfig().apply {
            name = "0.1"
            released = Date().toString()
        }
    })
    setConfigurations("archives")

/*  bintrayRepo = 'maven' //the maven repo name (created on bintray)
    bintrayName = 'ExpectAnim' //the name you want to give at your project on bintray
    orgName = 'florentchampigny' //your user name

    publishedGroupId = 'com.florentchampigny' //aaaa : the librairy group
    artifact = 'expectanim' //BBBB : the library name
    libraryVersion = "1.0.2" //the librairy version

    //the library will be : aaaa:BBBB:version

    libraryName = 'ExpectAnim'
    libraryDescription = 'Animate views easily'

    siteUrl = 'https://github.com/florent37/ExpectAnim'
    gitUrl = 'https://github.com/florent37/ExpectAnim.git'

    developerId = 'florentchampigny'
    developerName = 'Florent Champigny'
    developerEmail = 'champigny.florent@gmail.com'

    licenseName = 'The Apache Software License, Version 2.0'
    licenseUrl = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
    allLicenses = ["Apache-2.0"]*/
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
