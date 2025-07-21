plugins {
    java
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
	languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.ginsberg:junit5-system-exit:2.0.2")
    testImplementation("org.ow2.asm:asm:9.8") //fix to bug https://github.com/tginsberg/junit5-system-exit/issues/34
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
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
	"--add-opens", "java.base/java.io=ALL-UNNAMED"
	   )

    testLogging {
	exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	showExceptions = true
	showCauses = true
	showStackTraces = true
    }
}

tasks.withType<Test>().configureEach {
    afterSuite(
	KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
							     if (desc.parent == null)
							     {
								 println("\n🔧 Java (used by Gradle): ${
								     System.getProperty("java.runtime.version")
								 }")
								 println("🧠 Java VM: ${
								     System.getProperty("java.vm.name")
								 } (${System.getProperty("java.vm.version")})")
								 println(
								     "🛠 Gradle Version: ${gradle.gradleVersion}\n\n")

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