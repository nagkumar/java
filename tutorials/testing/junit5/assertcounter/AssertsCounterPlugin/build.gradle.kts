plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.shivoham.tools.junit5.assertcounter"
version = "1.0.8-SNAPSHOT"

repositories {
    mavenCentral()
}

// The plugin needs the Gradle API to function
dependencies {
    // This is the key: the plugin project depends on the agent project.
    // Gradle will automatically bundle the agent JAR with the plugin.
    implementation(project(":AssertsAgent"))
}

// Configure the plugin for Gradle's discovery mechanism
gradlePlugin {
    plugins {
	create("assertsCounter") {
	    id = "com.shivoham.asserts-counter" // This is the ID users will apply
	    implementationClass = "com.shivoham.tools.gradle.AssertsCounterPlugin"
	    displayName = "Asserts Counter Agent Plugin"
	    description = "Automatically applies the asserts-counter java agent to the test task."
	}
    }
}

// Your existing publishing logic, now for the plugin artifact
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