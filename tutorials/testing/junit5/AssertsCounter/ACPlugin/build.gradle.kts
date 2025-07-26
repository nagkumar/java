plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.shivohamai.cc"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// The plugin needs the Gradle API to function
dependencies {
    implementation(project(":ACAgent"))
}

gradlePlugin {
    plugins {
	create("assertsCounter") {
	    implementationClass = "com.shivoham.tools.gradle.AssertsCounterExtension"
	    displayName = "Asserts Counter Plugin"
	    description = "Automatically applies the asserts-counter java agent to the test task"
	}
    }
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
}