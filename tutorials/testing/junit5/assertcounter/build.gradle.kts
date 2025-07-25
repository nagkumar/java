plugins {
    `kotlin-dsl` apply false
}

subprojects {

    //Set group and version for both AssertsAgent and AssertsCounterPlugin.
    //Now you only have to change the version in this one file.
    group = "com.shivoham.tools.junit5.assertcounter"
    version = "1.0.8-SNAPSHOT"

    repositories {
	mavenCentral()
    }
}