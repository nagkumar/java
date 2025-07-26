@file:Suppress("SpellCheckingInspection")

plugins {
    id("com.github.ben-manes.versions") version ("0.52.0")
    id("se.patrikerdes.use-latest-versions") version ("0.2.18")
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

dependencies {
    implementation("com.typesafe:config:1.4.4")
    implementation("net.bytebuddy:byte-buddy:1.17.6")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.6")
    implementation("org.ow2.asm:asm:9.8") //fix to bug https://github.com/tginsberg/junit5-system-exit/issues/34

    implementation(platform("org.junit:junit-bom:6.0.0-M2"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.assertj:assertj-core:4.0.0-M1")
    implementation("org.hamcrest:hamcrest:3.0")
    testImplementation("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    doFirst {
	val launcher = javaLauncher.get()
	println("\n🔧 Test JVM toolchain path: ${launcher.metadata.installationPath}")
	println("🧠 Test JVM toolchain vendor: ${launcher.metadata.vendor}")
	println("🧠 Java version in toolchain: ${launcher.metadata.languageVersion.asInt()}\n\n")
    }
    useJUnitPlatform()
    val agentJarPath = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs("-javaagent:$agentJarPath")
}

tasks.jar {
    manifest {
	attributes["Premain-Class"] = "com.shivohamai.testing.tools.junit5.assertscounter.agent.ACAgent"
	attributes["Can-Redefine-Classes"] = "true"
	attributes["Can-Retransform-Classes"] = "true"
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
		username = System.getenv("GITHUB_ACTOR")
		password = System.getenv("GITHUB_TOKEN")
	    }
	}
    }
    publications {
	create<MavenPublication>("mavenJava") {
	    from(components["java"])
	    artifact("docs/artifacts/README.md") {
		classifier = "README"
		extension = "md"
	    }
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

tasks.named<ProcessResources>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

//defaultTasks("clean", "build")

defaultTasks("clean", "build", "publish")
