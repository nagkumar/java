plugins {
    java
    id("asserts-counter-plugin") version "1.0.34-SNAPSHOT"
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
	languageVersion = JavaLanguageVersion.of(24)
    }
}

sourceSets {
    main {
	resources {
	    srcDir("src/main/res")
	}
    }
}

repositories {
    mavenCentral()
    maven {
	name = "GitHubPackages"
	url = uri("https://maven.pkg.github.com/nagkumar/java")
	credentials {
	    username = System.getenv("GITHUB_ACTOR")
	    password = System.getenv("GITHUB_TOKEN")
	}
    }
}

dependencies {
    testImplementation("com.ginsberg:junit5-system-exit:2.0.2")
    testImplementation("org.ow2.asm:asm:9.8") //fix to bug https://github.com/tginsberg/junit5-system-exit/issues/34
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0-M2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    jvmArgumentProviders.add(CommandLineArgumentProvider {
	var dd = configurations.testRuntimeClasspath.get().files.find {
	    it.name.contains("junit5-system-exit")
	}
	listOf("-javaagent:${dd}")
    })

    jvmArgs(
	"--add-opens", "java.base/java.lang=ALL-UNNAMED",
	"--add-opens", "java.base/java.io=ALL-UNNAMED")

    testLogging {
	exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	showExceptions = true
	showCauses = true
	showStackTraces = true
    }
}
defaultTasks("clean", "build")