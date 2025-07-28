plugins {
    id("java")

    id("asserts-counter-plugin") version "1.0.4-SNAPSHOT"
}

group = "com.shivoham.sys2log"
version = "1.0-SNAPSHOT"

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

sourceSets {
    main {
	java {
	    srcDir("src")
	    exclude("**/tests/**/*.java")
	}
    }
    test {
	java {
	    srcDir("src")
	    include("**/tests/**/*.java")
	}
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")

    implementation(platform("org.apache.logging.log4j:log4j-bom:3.0.0-beta3"))
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-iostreams")

    //intellij does not support testImplementation for the test files within main folder, hence as work
    //around making the test jars part of main
    implementation(platform("org.junit:junit-bom:6.0.0-M1"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.platform:junit-platform-launcher")
    implementation("com.github.stefanbirkner:system-lambda:1.2.1")
    implementation("io.github.hakky54:logcaptor:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
								    if (desc.parent == null)
								    {
									println("🔍 Test Summary:")
									println(" - ${result.testCount} tests executed")
									println(
									    " - ${result.successfulTestCount} succeeded")
									println(" - ${result.failedTestCount} failed")
									println(" - ${result.skippedTestCount} skipped")
									println(
									    "\nTest Report: file:///" + reports.html.entryPoint.absolutePath.replace(
										File.separatorChar, '/'))
								    }
								}))
}

defaultTasks("clean", "build")
