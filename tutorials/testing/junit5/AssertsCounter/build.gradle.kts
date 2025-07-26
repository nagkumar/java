plugins {
    `kotlin-dsl` apply false
}

subprojects {
    //Set group and version for both AssertsAgent and AssertsCounterPlugin.
    //Now you only have to change the version in this one file.
    group = "com.shivohamai.cc"
    version = "1.0.0-SNAPSHOT"

    repositories {
	mavenCentral()
    }
}