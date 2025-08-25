import fix.eu.davidea.gradle.VersioningExtension

plugins {
    `java-library`
    `maven-publish`

    id("com.github.ben-manes.versions") version ("0.52.0")
    id("se.patrikerdes.use-latest-versions") version ("0.2.19")

    id("fix.eu.davidea.grabver") version "2.0.3"
}

val prjGroup: String = project.properties["prj.group"] as String
val prjJDKVer: Int =
    project.properties["prj.jdkVer"]?.toString()?.toIntOrNull()
    ?: 8

versioning {
    preRelease = "SNAPSHOT"
    saveOn = "publish"
}

allprojects {
    afterEvaluate {
	group = prjGroup
	var verE = rootProject.extensions.getByName("versioning") as VersioningExtension
	version = "${verE.major}.${verE.minor}.${verE.build}-${verE.preRelease}"
    }

    repositories {
	githubPackages()
	gradlePluginPortal()
	mavenCentral()
	mavenLocal()
    }
    defaultTasks("clean", "build", "publish")
}

subprojects {
    apply(plugin = "java-library")
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
	    githubPackages()
	    mavenLocal()
	}

	publications {
	    create<MavenPublication>("mavenJava") {
		from(components["java"])
		artifactId = project.name
	    }
	}
    }

    tasks.named("publish") {
	doLast {
	    println("View it at: https://github.com/nagkumar/java/packages/2589016")
	}
	outputs.upToDateWhen { false }
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
	doFirst {
	    val repo = repository
	    println("\nPublishing to repository: ${repo.name ?: "unknown"}")
	    println("Repository URL: ${repo.url}")
	    publishing.publications.withType<MavenPublication>().forEach { pub ->
		println("Publication: ${pub.name}")
		pub.artifacts.forEach {
		    println("\tArtifact: ${it.file.name}")
		}
	    }
	}
    }

    tasks.withType<Test>().configureEach {
	//maxParallelForks = 1 // Only one test fork
	//systemProperty("junit.jupiter.execution.parallel.enabled", "false") // Disable JUnit 5 parallelism
	//systemProperty("junit.jupiter.execution.parallel.mode.default", "same_thread")
	//systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "same_thread")
    }

    listOf(Tar::class, Zip::class, Jar::class).forEach { taskType ->
	tasks.withType(taskType.java) {
	    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
    }

    dependencies {
	implementation("com.github.michaelgantman:MgntUtils:1.7.0.2")
    }
}

tasks.configureEach {
    if (name in listOf("build", "jar", "test", "assemble", "classes"))
    {
	enabled = false
    }
}

fun RepositoryHandler.githubPackages()
{
    maven {
	name = "GitHubPackages"
	url = uri("https://maven.pkg.github.com/nagkumar/java")
	credentials {
	    username = System.getenv("GITHUB_ACTOR")
	    password = System.getenv("GITHUB_TOKEN")
	}
    }
}