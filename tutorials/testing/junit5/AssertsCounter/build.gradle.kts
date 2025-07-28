plugins {
    java
    `maven-publish`
    id("net.saliman.properties") version "1.6.0"
}

val prjGroup: String = project.properties["prj.group"] as String
val prjVer: String = project.properties["prj.ver"] as String
val prjJDKVer: Int = project.properties["prj.jdkVer"]?.toString()?.toIntOrNull() ?: 8

allprojects {
    version = prjVer
    group = prjGroup
    
    repositories {
	gradlePluginPortal()
	mavenCentral()
    }
    defaultTasks("clean", "build", "publish")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
	toolchain {
	    languageVersion.set(JavaLanguageVersion.of(prjJDKVer))
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

    tasks.withType<JavaCompile>().configureEach {
	sourceCompatibility = prjJDKVer.toString()
	targetCompatibility = prjJDKVer.toString()
    }

    publishing {
	repositories {
	    maven {
		name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/nagkumar/java")
		credentials {
		    username = System.getenv("GITHUB_ACTOR")
		    password = System.getenv("GITHUB_TOKEN")
		}
	    }
	}
	publications {
	    create<MavenPublication>("mavenJava") {
		from(components["java"])
	    }
	}
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
	doFirst {
	    println("\nPublishing to GitHub Packages...")
	    publishing.publications.withType<MavenPublication>().forEach { pub ->
		println("Publication: ${pub.name}")
		pub.artifacts.forEach {
		    println("\tArtifact: ${it.file.name}")
		}
	    }
	}
    }

    tasks.named("publish") {
	doLast {
	    println("View it at: https://github.com/nagkumar/java/packages/2589016")
	}
	outputs.upToDateWhen { false }
    }

    tasks.withType<Test>().configureEach {
	maxParallelForks = 1 // Only one test fork
	systemProperty("junit.jupiter.execution.parallel.enabled", "false") // Disable JUnit 5 parallelism
	systemProperty("junit.jupiter.execution.parallel.mode.default", "same_thread")
	systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "same_thread")
    }

    tasks.withType<Tar> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.withType<Zip> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    // Also handle duplicates in JAR tasks if needed
    tasks.withType<Jar> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}