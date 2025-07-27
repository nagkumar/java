plugins {
    java
    `maven-publish`
}

val projectGroup = "com.shivohamai.cc"
val projectVersion = "1.0.17-SNAPSHOT"

val intJavaVer = 21

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

    tasks.withType<JavaCompile>().configureEach {
	sourceCompatibility = intJavaVer.toString()
	targetCompatibility = intJavaVer.toString()
    }


    tasks.named("publish") {
	outputs.upToDateWhen { false }
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
    }
}