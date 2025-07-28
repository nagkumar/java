package com.shivohamai.testing.tools.junit5.assertscounter.plugin.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.kotlin.dsl.KotlinClosure2
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import java.io.File

/**
 * A Gradle plugin that automatically instruments the `test` task to count
 * JUnit 5 assertions using a Java Agent.
 */
open class ACPlugin : Plugin<Project>
{
    override fun apply(aProject: Project)
    {
	AddExtraPlugins().apply(aProject)

	// 1. Create a private, resolvable configuration to hold the agent JAR
	val lAgentJARConf = aProject.configurations.create("assertsAgentJAR") {
	    isVisible = false
	    isCanBeConsumed = false
	    isCanBeResolved = true
	    description = "The asserts-counter agent JAR and its dependencies"
	}

	// 2. Add the agent as a dependency to our private configuration
	val lPluginVersion = javaClass.`package`.implementationVersion
	val acDependency = "com.shivohamai.cc:asserts-counter-agent:$lPluginVersion"
	aProject.dependencies.add(lAgentJARConf.name, acDependency)
	aProject.dependencies.add("implementation", acDependency)

	// 3. Register the extension for manual report printing
	aProject.extensions.create<ACPlugin>("assertsCounter")

	// 4. Configure all tasks of type `Test` to use the agent and be finalized by the report task
	aProject.tasks.withType<Test>().configureEach {
	    doFirst {
		val launcher = javaLauncher.get()
		println("\n🔧 Test JVM toolchain path: ${launcher.metadata.installationPath}")
		println("🧠 Test JVM toolchain vendor: ${launcher.metadata.vendor}")
		println("🧠 Java version in toolchain: ${launcher.metadata.languageVersion.asInt()}\n\n")

		val bAgentJarFile = lAgentJARConf.files.singleOrNull {
		    it.name.contains("asserts-counter-agent") && it.extension == "jar"
		}
				    ?: throw GradleException(
					"Could not find a JAR matching asserts-counter-agent in configuration 'assertsAgentJAR'")
		jvmArgumentProviders.add {
		    listOf("-javaagent:${bAgentJarFile.absolutePath}")
		}
	    }

	    //5. Add afterSuite to print test summary and environment details
	    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
									    if (desc.parent == null)
									    {
										println("\n🔧 Java (used by Gradle): ${
										    System.getProperty(
											"java.runtime.version")
										}")
										println("🧠 Java VM: ${
										    System.getProperty("java.vm.name")
										} (${
										    System.getProperty(
											"java.vm.version")
										})")
										println(
										    "🛠 Gradle Version: ${aProject.gradle.gradleVersion}\n")
										println("🔍 Test Summary:")
										println(
										    " - ${result.testCount} tests executed")
										println(
										    " - ${result.successfulTestCount} succeeded")
										println(
										    " - ${result.failedTestCount} failed")
										println(
										    " - ${result.skippedTestCount} skipped")
										println("\nTest Report: file:///${
										    reports.html.entryPoint.absolutePath.replace(
											File.separatorChar, '/')
										}")
									    }
									})
		      )
	}
    }
}