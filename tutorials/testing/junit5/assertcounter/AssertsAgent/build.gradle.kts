plugins {
    java
    `maven-publish`

    id("com.github.ben-manes.versions") version ("0.52.0")
    id("se.patrikerdes.use-latest-versions") version ("0.2.18")
}

group = "com.shivoham.tools.junit5.assertcounter"
version = "1.0.6-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
	languageVersion.set(JavaLanguageVersion.of(21))
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
    implementation("net.bytebuddy:byte-buddy:1.17.6")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.6")
    implementation("org.ow2.asm:asm:9.8") //fix to bug https://github.com/tginsberg/junit5-system-exit/issues/34

    testImplementation(platform("org.junit:junit-bom:6.0.0-M1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    testImplementation("org.hamcrest:hamcrest:3.0")
}

tasks.test {
    useJUnitPlatform()
    val agentJarPath = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs("-javaagent:$agentJarPath")
}

tasks.jar {
    manifest {
	attributes("Premain-Class" to "com.shivoham.tools.junit5.assertcounter.AssertCountAgent",
		   "Can-Redefine-Classes" to "true", "Can-Retransform-Classes" to "true")
    }
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

tasks.named("publish") {
    outputs.upToDateWhen { false }
}

publishing {
    repositories {
	maven {
	    name = "GitHubPackages"
	    url = uri("https://maven.pkg.github.com/nagkumar/java")
	    credentials {
		username =
		    System.getenv("GITHUB_ACTOR")
		    ?: "nagkumar"
		password = System.getenv("GITHUB_TOKEN")
	    }
	    println("\nPublishing to: $url")
	}
    }
    publications {
	create<MavenPublication>("mavenJava") {
	    artifactId = "asserts-agent"
	    from(components["java"])
	    artifact("docs/artifacts/README.md") {
		classifier = "README"
		extension = "md"
	    }
	}

	withType<MavenPublication>().configureEach {
	    println("Publication: ${name}")
	    artifacts.forEach {
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

defaultTasks("clean", "build", "publish")
