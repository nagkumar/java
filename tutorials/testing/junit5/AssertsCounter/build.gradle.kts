import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.JavaVersion

plugins {
    `kotlin-dsl` apply false
}

allprojects {
    //Set group and version for both AssertsAgent and AssertsCounterPlugin.
    //Now you only have to change the version in this one file.
    group = "com.shivohamai.cc"
    version = "1.0.0-SNAPSHOT"

    repositories {
	mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
	toolchain {
	    languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion.toString().removePrefix("VERSION_")))
	}
    }

    tasks.withType<JavaCompile>().configureEach {
	sourceCompatibility = javaLanguageVersion.toString()
	targetCompatibility = javaLanguageVersion.toString()
    }

    tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.jvmTarget = javaLanguageVersion.toString()
    }
}