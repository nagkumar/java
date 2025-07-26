import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    java
    `maven-publish`
    `kotlin-dsl` apply false
}

val projectGroup = "com.shivohamai.cc"
val projectVersion = "1.0.0-SNAPSHOT"
val javaLanguageVersion = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
val intJavaVer = javaLanguageVersion.toString().removePrefix("JVM_")

allprojects {
    group = projectGroup
    version = projectVersion

    repositories {
	mavenCentral()
    }
    defaultTasks("clean", "build", "publish")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
	toolchain {
	    languageVersion.set(JavaLanguageVersion.of(intJavaVer))
	}
    }

    sourceSets {
	main {
	    java {
		srcDir("src")
		exclude("**/tests/**/*.java")
	    }
	    resources {
		srcDir("src/res")
		include("**/*.conf")
	    }
	}
	test {
	    java {
		srcDir("src")
		include("**/tests/**/*.java")
	    }
	}
    }

    tasks.named<ProcessResources>("processResources") {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
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