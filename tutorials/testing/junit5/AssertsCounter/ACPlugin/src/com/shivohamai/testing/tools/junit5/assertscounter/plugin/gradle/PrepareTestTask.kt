package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

// 4. Configure all tasks of type `Test` to use the agent and be finalized by the report task
fun prepareTestTask(aProject: Project, aAgentJARConf: Configuration)
{
    aProject.tasks.withType<Test>().configureEach {
	doFirst {
	    val launcher = javaLauncher.get()
	    println("\n🔧 Test JVM toolchain path: ${launcher.metadata.installationPath}")
	    println("🧠 Test JVM toolchain vendor: ${launcher.metadata.vendor}")
	    println("🧠 Java version in toolchain: ${launcher.metadata.languageVersion.asInt()}\n\n")

	    val bAgentJarFile = aAgentJARConf.files.singleOrNull {
		it.name.contains("asserts-counter-agent") && it.extension == "jar"
	    }
				?: throw GradleException(
				    "Could not find a JAR matching asserts-counter-agent in configuration 'assertsAgentJAR'")
	    jvmArgumentProviders.add {
		listOf("-javaagent:${bAgentJarFile.absolutePath}")
	    }
	}

	printTestResults(aProject)
    }
}