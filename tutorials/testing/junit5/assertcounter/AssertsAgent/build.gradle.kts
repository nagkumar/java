plugins {
    java
    `maven-publish`

    id("com.github.ben-manes.versions") version ("0.52.0")
    id("se.patrikerdes.use-latest-versions") version ("0.2.18")
}

group = "com.shivoham.tests.assertcounter"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.5")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.5")

    implementation(platform("org.junit:junit-bom:6.0.0-M1"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.platform:junit-platform-launcher")

    implementation("org.assertj:assertj-core:3.24.2")
    implementation("org.hamcrest:hamcrest:2.2")
}

java {
    toolchain {
	languageVersion.set(JavaLanguageVersion.of(17))
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

tasks.test {
    useJUnitPlatform()
    val agentJarPath = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs("-javaagent:$agentJarPath")
}

tasks.jar {
    manifest {
	attributes("Premain-Class" to "com.shivoham.tools.junit5.assertcounter.AssertCountAgent",
		   "Can-Redefine-Classes" to "true",
		   "Can-Retransform-Classes" to "true")
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

publishing {
    publications {
	create<MavenPublication>("agent") {
	    artifact(tasks.jar.get())
	    groupId = project.group.toString()
	    artifactId = "assert-counter-agent"
	    version = project.version.toString()
	}
    }

    repositories {
	maven {
	    name = "GitHubPackages"
	    url = uri("https://maven.pkg.github.com/nagkumar/java")
	    credentials {
		username =
		    System.getenv("GITHUB_ACTOR")
		    ?: "<your-github-username>"
		password =
		    System.getenv("GITHUB_TOKEN")
		    ?: "<your-personal-access-token>"
	    }
	}
    }
}
