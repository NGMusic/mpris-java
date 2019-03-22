# mpris-java

Simple [MPRIS 2.2](https://specifications.freedesktop.org/mpris-spec/2.2/) implementation with Kotlin for the JVM. 
Code is basically done, but largely untested. No guarantees for it to work.

A test implementation can be found in [extensions/test/xerus/mpris/MPRISPlayer.kt](extensions/test/xerus/mpris/MPRISPlayer.kt).

The project is separated into two modules, the base/root module and [mpris-extensions](extensions), which is meant to ease the use of this library by providing reference implementations and abstractions.

The base module should be compatible with any JVM language, while the extensions module is specifically fitted to Kotlin.