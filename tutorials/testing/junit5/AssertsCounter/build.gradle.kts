import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    java
    application
    `maven-publish`
    `kotlin-dsl` apply false
}

val projectGroup = "com.shivohamai"
val projectVersion = "1.0.0"
val javaLanguageVersion = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
val intJavaVer = javaLanguageVersion.toString().removePrefix("JVM_");

allprojects {
    group = projectGroup
    version = projectVersion

    repositories {
	mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
	toolchain {
	    languageVersion.set(JavaLanguageVersion.of(intJavaVer))
	}
    }

    tasks.withType<JavaCompile>().configureEach {
	sourceCompatibility = intJavaVer
	targetCompatibility = intJavaVer
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
	compilerOptions {
	    jvmTarget.set(javaLanguageVersion)
	}
    }
}