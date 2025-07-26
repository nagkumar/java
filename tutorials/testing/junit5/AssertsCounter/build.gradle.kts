import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    java
    application
    `kotlin-dsl` apply false
}

val projectGroup = "com.shivohamai"
val projectVersion = "1.0.0"
val javaLanguageVersion = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

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
	    languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion.toString()))
	}
    }

    tasks.withType<JavaCompile>().configureEach {
	sourceCompatibility = javaLanguageVersion.toString()
	targetCompatibility = javaLanguageVersion.toString()
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
	compilerOptions {
	    jvmTarget.set(javaLanguageVersion)
	}
    }
}